package io.thinkit.edc.client.connector.endtoend;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static io.thinkit.edc.client.connector.utils.Constants.ODRL_NAMESPACE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.RealTimeConnectorApiTestBase;
import io.thinkit.edc.client.connector.model.CatalogRequest;
import io.thinkit.edc.client.connector.model.ContractRequest;
import io.thinkit.edc.client.connector.model.Policy;
import io.thinkit.edc.client.connector.model.jsonld.*;
import io.thinkit.edc.client.connector.model.pojo.PojoCatalogRequest;
import io.thinkit.edc.client.connector.model.pojo.PojoContractRequest;
import io.thinkit.edc.client.connector.model.pojo.PojoPolicy;
import io.thinkit.edc.client.connector.services.management.Assets;
import io.thinkit.edc.client.connector.services.management.Catalogs;
import io.thinkit.edc.client.connector.services.management.ContractDefinitions;
import io.thinkit.edc.client.connector.services.management.ContractNegotiations;
import io.thinkit.edc.client.connector.services.management.PolicyDefinitions;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedClass
@ValueSource(strings = {V3, V4BETA})
class ContractNegotiationsEndToEndTest extends RealTimeConnectorApiTestBase {

    private static final String PROVIDER_PROTOCOL_URL = "http://provider-connector:9194/protocol/2025-1";
    private static final String PROVIDER_PARTICIPANT_ID = "provider";
    private static final String DSP_PROTOCOL = "dataspace-protocol-http:2025-1";

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private ContractNegotiations contractNegotiations;
    private Assets providerAssets;
    private PolicyDefinitions providerPolicyDefinitions;
    private ContractDefinitions providerContractDefinitions;
    private Catalogs consumerCatalogs;

    ContractNegotiationsEndToEndTest(String managementVersion) {
        this.managementVersion = managementVersion;
    }

    @BeforeEach
    void setUp() {
        var providerClient = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(getProviderManagementUrl(), managementVersion)
                .build();
        var consumerClient = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(getConsumerManagementUrl(), managementVersion)
                .build();

        providerAssets = providerClient.assets();
        providerPolicyDefinitions = providerClient.policyDefinitions();
        providerContractDefinitions = providerClient.contractDefinitions();
        consumerCatalogs = consumerClient.catalogs();
        contractNegotiations = consumerClient.contractNegotiations();
    }

    @Test
    void should_initiate_a_contract_negotiation() {
        var assetId = prepareProviderCatalog();
        var offer = findOfferForAsset(assetId);
        var negotiationId = initiateNegotiation(offer, assetId);

        assertThat(negotiationId).isNotNull();
    }

    @Test
    void should_get_a_contract_negotiation() {
        var assetId = prepareProviderCatalog();
        var offer = findOfferForAsset(assetId);
        var negotiationId = initiateNegotiation(offer, assetId);

        var negotiation = contractNegotiations.get(negotiationId);
        assertThat(negotiation.isSucceeded()).isTrue();
        assertThat(negotiation.getContent()).isNotNull();

        var state = waitForState(negotiationId, "FINALIZED");
        assertThat(state).isEqualTo("FINALIZED");
    }

    private Policy findOfferForAsset(String assetId) {
        var catalog = consumerCatalogs.request(catalogRequest());
        assertThat(catalog.isSucceeded()).isTrue();
        assertThat(catalog.getContent()).isNotNull();

        var dataset = catalog.getContent().datasets().stream()
                .filter(d -> assetId.equals(d.id()))
                .findFirst()
                .orElse(null);
        assertThat(dataset).isNotNull();

        var offer = dataset.hasPolicy().get(0);
        assertThat(offer).isNotNull();
        return offer;
    }

    private String initiateNegotiation(Policy offer, String assetId) {
        var created = contractNegotiations.create(contractRequest(offer, assetId));
        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
        return created.getContent();
    }

    private String prepareProviderCatalog() {
        var assetId = "assetId-" + UUID.randomUUID();
        providerAssets.create(JsonLdAsset.Builder.newInstance()
                .id(assetId)
                .properties(Map.of("key1", "value1"))
                .dataAddress(Map.of("type", "HttpData", "baseUrl", "https://jsonplaceholder.typicode.com/users"))
                .build());

        var policyId = "policyId-" + UUID.randomUUID();
        providerPolicyDefinitions.create(JsonLdPolicyDefinition.Builder.newInstance()
                .id(policyId)
                .policy(JsonLdPolicy.Builder.newInstance().build())
                .build());

        providerContractDefinitions.create(JsonLdContractDefinition.Builder.newInstance()
                .id("contractDefinitionId-" + UUID.randomUUID())
                .accessPolicyId(policyId)
                .contractPolicyId(policyId)
                .assetsSelector(emptyList())
                .build());

        return assetId;
    }

    private CatalogRequest catalogRequest() {
        if (V3.equals(managementVersion)) {
            return JsonLdCatalogRequest.Builder.newInstance()
                    .counterPartyAddress(PROVIDER_PROTOCOL_URL)
                    .counterPartyId(PROVIDER_PARTICIPANT_ID)
                    .protocol(DSP_PROTOCOL)
                    .build();
        } else {
            return PojoCatalogRequest.Builder.newInstance()
                    .counterPartyAddress(PROVIDER_PROTOCOL_URL)
                    .counterPartyId(PROVIDER_PARTICIPANT_ID)
                    .protocol(DSP_PROTOCOL)
                    .build();
        }
    }

    private String waitForState(String negotiationId, String expectedState) {
        var deadline = System.currentTimeMillis() + timeout * 1000L;
        String state = null;
        while (System.currentTimeMillis() < deadline) {
            var result = contractNegotiations.getState(negotiationId);
            if (result.isSucceeded()) {
                state = result.getContent();
                if (expectedState.equals(state)) {
                    return state;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return state;
            }
        }
        return state;
    }

    private ContractRequest contractRequest(Policy offer, String assetId) {
        if (V3.equals(managementVersion)) {
            return JsonLdContractRequest.Builder.newInstance()
                    .counterPartyAddress(PROVIDER_PROTOCOL_URL)
                    .protocol(DSP_PROTOCOL)
                    .policy(JsonLdPolicy.Builder.newInstance()
                            .raw(((JsonLdPolicy) offer).raw())
                            .type(ODRL_NAMESPACE + "Offer")
                            .assigner(PROVIDER_PARTICIPANT_ID)
                            .target(assetId)
                            .build())
                    .build();
        } else {
            return PojoContractRequest.Builder.newInstance()
                    .counterPartyAddress(PROVIDER_PROTOCOL_URL)
                    .protocol(DSP_PROTOCOL)
                    .policy(PojoPolicy.Builder.newInstance()
                            .id(offer.id())
                            .type(ODRL_NAMESPACE + "Offer")
                            .assigner(PROVIDER_PARTICIPANT_ID)
                            .target(assetId)
                            .build())
                    .build();
        }
    }
}
