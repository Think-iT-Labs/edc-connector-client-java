package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.ODRL_NAMESPACE;
import static jakarta.json.Json.createObjectBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.json.Json;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class ContractNegotiationsTest {

    @Container
    private final ManagementApiContainer prism = new ManagementApiContainer();

    private final HttpClient http = HttpClient.newBuilder().build();
    private ContractNegotiations contractNegotiations;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        contractNegotiations = client.contractNegotiations();
    }

    @Test
    void should_get_a_contract_negotiation() {
        var contractNegotiation = contractNegotiations.get("negotiation-id");

        assertThat(contractNegotiation.isSucceeded()).isTrue();
        assertThat(contractNegotiation.getContent().id()).isNotBlank();
        assertThat(contractNegotiation.getContent().type()).isNotNull().satisfies(type -> assertThat(type)
                .isEqualTo("PROVIDER"));
        assertThat(contractNegotiation.getContent().protocol()).isNotNull().satisfies(protocol -> assertThat(protocol)
                .isEqualTo("dataspace-protocol-http"));
        assertThat(contractNegotiation.getContent().counterPartyId())
                .isNotNull()
                .satisfies(counterPartyId -> assertThat(counterPartyId).isEqualTo("counter-party-id"));
        assertThat(contractNegotiation.getContent().counterPartyAddress())
                .isNotNull()
                .satisfies(counterPartyAddress ->
                        assertThat(counterPartyAddress).isEqualTo("http://counter/party/address"));
        assertThat(contractNegotiation.getContent().state()).isNotNull().satisfies(state -> assertThat(state)
                .isEqualTo("VERIFIED"));
        assertThat(contractNegotiation.getContent().contractAgreementId())
                .isNotNull()
                .satisfies(
                        contractAgreementId -> assertThat(contractAgreementId).isEqualTo("contract:agreement:id"));
        assertThat(contractNegotiation.getContent().errorDetail())
                .isNotNull()
                .satisfies(errorDetail -> assertThat(errorDetail).isEqualTo("eventual-error-detail"));
        assertThat(contractNegotiation.getContent().callbackAddresses())
                .isNotNull()
                .first()
                .satisfies(callbackAddress -> {
                    assertThat(callbackAddress.authCodeId()).isNotNull().satisfies(authCodeId -> assertThat(authCodeId)
                            .isEqualTo("auth-code-id"));
                    assertThat(callbackAddress.authKey()).isNotNull().satisfies(authKey -> assertThat(authKey)
                            .isEqualTo("auth-key"));
                    assertThat(callbackAddress.transactional()).isNotNull().satisfies(transactional -> assertThat(
                                    transactional)
                            .isFalse());
                    assertThat(callbackAddress.uri()).isNotNull().satisfies(uri -> assertThat(uri)
                            .isEqualTo("http://callback/url"));
                    assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                        assertThat(uri.get(0)).isEqualTo("contract.negotiation");
                        assertThat(uri.get(1)).isEqualTo("transfer.process");
                    });
                });
        assertThat(contractNegotiation.getContent().createdAt()).isGreaterThan(0);
    }

    @Test
    void should_not_get_a_contract_negotiation_when_id_is_empty() {
        var contractNegotiation = contractNegotiations.get("");

        assertThat(contractNegotiation.isSucceeded()).isFalse();
        assertThat(contractNegotiation.getError()).isNotNull();
    }

    @Test
    void should_create_a_contract_negotiation() {

        var permissions = Json.createArrayBuilder()
                .add(createObjectBuilder().add("target", "asset-id").add("action", "display"))
                .build();

        var policy = Policy.Builder.newInstance()
                .raw(createObjectBuilder().add("permission", permissions).build())
                .build();
        var callbackAddresses = CallbackAddress.Builder.newInstance()
                .transactional(false)
                .uri("http://callback/url")
                .authKey("auth-key")
                .authCodeId("auth-code-id")
                .events(Arrays.asList("contract.negotiation", "transfer.process"))
                .build();
        var contractNegotiation = ContractRequest.Builder.newInstance()
                .counterPartyAddress("http://provider-address")
                .protocol("dataspace-protocol-http")
                .providerId("provider-id")
                .policy(policy)
                .callbackAddresses(List.of(callbackAddresses, callbackAddresses))
                .build();

        var created = contractNegotiations.create(contractNegotiation);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should_not_create_a_contract_negotiation_when_provider_id_is_empty() {

        var contractNegotiation = ContractRequest.Builder.newInstance()
                .counterPartyAddress("http://provider-address")
                .protocol("dataspace-protocol-http")
                .build();

        var created = contractNegotiations.create(contractNegotiation);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getError()).isNotNull();
    }

    @Test
    void should_get_a_contract_negotiation_attached_agreement() {
        var contractAgreement = contractNegotiations.getAgreement("negotiation-id");

        assertThat(contractAgreement.isSucceeded()).isTrue();
        assertThat(contractAgreement.getContent().id()).isNotBlank();
        assertThat(contractAgreement.getContent().providerId()).isEqualTo("provider-id");
        assertThat(contractAgreement.getContent().consumerId()).isEqualTo("consumer-id");
        assertThat(contractAgreement.getContent().assetId()).isEqualTo("asset-id");
        assertThat(contractAgreement.getContent().contractSigningDate()).isGreaterThan(0);
        assertThat(contractAgreement.getContent().policy()).isNotNull().satisfies(policy -> assertThat(
                        policy.getList(ODRL_NAMESPACE + "permission").size())
                .isGreaterThan(0));
    }

    @Test
    void should_not_get_a_contract_negotiation_attached_agreement() {
        var contractAgreement = contractNegotiations.getAgreement("");

        assertThat(contractAgreement.isSucceeded()).isFalse();
        assertThat(contractAgreement.getError()).isNotNull();
    }

    @Test
    void should_terminate_a_contract_negotiation() {
        var input = TerminateNegotiation.Builder.newInstance()
                .id("negotiation-id")
                .reason("a reason to terminate")
                .build();
        var terminated = contractNegotiations.terminate(input);

        assertThat(terminated.isSucceeded()).isTrue();
        assertThat(terminated.getContent()).isNotNull();
    }

    @Test
    void should_not_terminate_a_contract_negotiation_when_id_is_empty() {
        var input = TerminateNegotiation.Builder.newInstance()
                .id("")
                .reason("a reason to terminate")
                .build();
        var terminated = contractNegotiations.terminate(input);

        assertThat(terminated.isSucceeded()).isFalse();
        assertThat(terminated.getError()).isNotNull();
    }

    @Test
    void should_get_contract_negotiations() {
        var input = QuerySpec.Builder.newInstance()
                .offset(0)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();

        var ContractNegotiationList = contractNegotiations.request(input);

        assertThat(ContractNegotiationList.isSucceeded()).isTrue();
        assertThat(ContractNegotiationList.getContent()).isNotNull().first().satisfies(contractNegotiation -> {
            assertThat(contractNegotiation.id()).isNotBlank();
            assertThat(contractNegotiation.type()).isNotNull().satisfies(type -> assertThat(type)
                    .isEqualTo("PROVIDER"));
            assertThat(contractNegotiation.protocol()).isNotNull().satisfies(protocol -> assertThat(protocol)
                    .isEqualTo("dataspace-protocol-http"));
            assertThat(contractNegotiation.counterPartyId())
                    .isNotNull()
                    .satisfies(counterPartyId -> assertThat(counterPartyId).isEqualTo("counter-party-id"));
            assertThat(contractNegotiation.counterPartyAddress())
                    .isNotNull()
                    .satisfies(counterPartyAddress ->
                            assertThat(counterPartyAddress).isEqualTo("http://counter/party/address"));
            assertThat(contractNegotiation.state()).isNotNull().satisfies(state -> assertThat(state)
                    .isEqualTo("VERIFIED"));
            assertThat(contractNegotiation.contractAgreementId())
                    .isNotNull()
                    .satisfies(contractAgreementId ->
                            assertThat(contractAgreementId).isEqualTo("contract:agreement:id"));
            assertThat(contractNegotiation.errorDetail()).isNotNull().satisfies(errorDetail -> assertThat(errorDetail)
                    .isEqualTo("eventual-error-detail"));
            assertThat(contractNegotiation.callbackAddresses())
                    .isNotNull()
                    .first()
                    .satisfies(callbackAddress -> {
                        assertThat(callbackAddress.authCodeId())
                                .isNotNull()
                                .satisfies(authCodeId -> assertThat(authCodeId).isEqualTo("auth-code-id"));
                        assertThat(callbackAddress.authKey()).isNotNull().satisfies(authKey -> assertThat(authKey)
                                .isEqualTo("auth-key"));
                        assertThat(callbackAddress.transactional()).isNotNull().satisfies(transactional -> assertThat(
                                        transactional)
                                .isFalse());
                        assertThat(callbackAddress.uri()).isNotNull().satisfies(uri -> assertThat(uri)
                                .isEqualTo("http://callback/url"));
                        assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                            assertThat(uri.get(0)).isEqualTo("contract.negotiation");
                            assertThat(uri.get(1)).isEqualTo("transfer.process");
                        });
                    });
            assertThat(contractNegotiation.createdAt()).isGreaterThan(0);
        });
    }

    @Test
    void should_not_get_contract_negotiations() {
        var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

        var result = contractNegotiations.request(input);

        assertThat(result.isSucceeded()).isFalse();
        assertThat(result.getError()).isNotNull();
    }

    @Test
    void should_get_a_contract_negotiation_state() {

        var state = contractNegotiations.getState("negotiation-id");

        assertThat(state.isSucceeded()).isTrue();
        assertThat(state.getContent()).isNotNull().isEqualTo("string");
    }

    @Test
    void should_not_get_a_contract_negotiation_state() {

        var state = contractNegotiations.getState("");

        assertThat(state.isSucceeded()).isFalse();
        assertThat(state.getError()).isNotNull();
    }
}
