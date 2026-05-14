package io.thinkit.edc.client.connector.endtoend;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.RealTimeConnectorApiTestBase;
import io.thinkit.edc.client.connector.model.ContractDefinition;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdContractDefinition;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdCriterion;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
import io.thinkit.edc.client.connector.services.management.ContractDefinitions;
import java.net.http.HttpClient;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedClass
@ValueSource(strings = {V3, V4})
class ContractDefinitionsEndToEndTest extends RealTimeConnectorApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private ContractDefinitions contractDefinitions;

    ContractDefinitionsEndToEndTest(String managementVersion) {
        this.managementVersion = managementVersion;
    }

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(getProviderManagementUrl(), managementVersion)
                .build();
        contractDefinitions = client.contractDefinitions();
    }

    @Test
    void should_create_a_contract_definition() {
        var id = "contractDefinitionId-" + UUID.randomUUID();
        var created = contractDefinitions.create(createAContractDefinitionRequest(id));

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isEqualTo(id);
    }

    @Test
    void should_not_create_a_contract_definition_when_request_is_invalid() {
        var id = "contractDefinitionId-" + UUID.randomUUID();
        var created = contractDefinitions.create(createInvalidContractDefinition(id));

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.type()).isEqualTo("ValidationFailure");
        });
    }

    @Test
    void should_get_a_contract_definition() {
        var id = "contractDefinitionId-" + UUID.randomUUID();
        var created = contractDefinitions.create(createAContractDefinitionRequest(id));

        var contractDefinition = contractDefinitions.get(id);

        assertThat(contractDefinition.isSucceeded()).isTrue();
        assertThat(contractDefinition.getContent()).isNotNull();
        assertThat(contractDefinition.getContent().id()).isEqualTo(created.getContent());
        assertThat(contractDefinition.getContent().contractPolicyId()).isNotBlank();
    }

    @Test
    void should_not_get_a_contract_definition() {

        var contractDefinition = contractDefinitions.get("unexistent-id");

        assertThat(contractDefinition.isSucceeded()).isFalse();
        assertThat(contractDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
        });
    }

    @Test
    void should_delete_a_contract_definition() {
        var id = "contractDefinitionId-" + UUID.randomUUID();
        var created = contractDefinitions.create(createAContractDefinitionRequest(id));
        var deleted = contractDefinitions.delete(created.getContent());
        var contractDefinition = contractDefinitions.get(id);
        assertThat(deleted.isSucceeded()).isTrue();
        assertThat(contractDefinition.isSucceeded()).isFalse();
        assertThat(contractDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
        });
    }

    @Test
    void should_not_delete_a_contract_definition() {
        var id = "contractDefinitionId-" + UUID.randomUUID();
        var deleted = contractDefinitions.delete(id);
        assertThat(deleted.isSucceeded()).isFalse();
        assertThat(deleted.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
        });
    }

    @Test
    void should_get_contract_definitions() {
        var id = "contractDefinitionId-" + UUID.randomUUID();
        var created = contractDefinitions.create(createAContractDefinitionRequest(id));
        var ContractDefinitionList = contractDefinitions.request(getContractDefinitionsQuery());

        assertThat(ContractDefinitionList.getContent())
                .anyMatch(asset -> asset.id().equals(created.getContent()));
    }

    @Test
    void should_update_a_contract_definition() {
        var id = "contractDefinitionId-" + UUID.randomUUID();

        var created = contractDefinitions.create(createAContractDefinitionRequest(id));
        var updated = contractDefinitions.update(updateAContractDefinitionRequest(created.getContent()));

        var contractDefinition = contractDefinitions.get(created.getContent());

        assertThat(updated.isSucceeded()).isTrue();
        assertThat(contractDefinition.isSucceeded()).isTrue();
        assertThat(contractDefinition.getContent()).isNotNull();
        assertThat(contractDefinition.getContent().assetsSelector())
                .isNotNull()
                .first()
                .satisfies(criterion -> assertThat(criterion.operator()).isEqualTo("="));
    }

    private ContractDefinition createAContractDefinitionRequest(String id) {
        var assetPolicyId = "asset-policy-id-" + UUID.randomUUID();
        return JsonLdContractDefinition.Builder.newInstance()
                .id(id)
                .accessPolicyId(assetPolicyId)
                .contractPolicyId(assetPolicyId)
                .assetsSelector(emptyList())
                .build();
    }

    private ContractDefinition createInvalidContractDefinition(String id) {
        var contractPolicyId = "contract-policy-id-" + UUID.randomUUID();
        return JsonLdContractDefinition.Builder.newInstance()
                .id(id)
                .contractPolicyId(contractPolicyId)
                .assetsSelector(emptyList())
                .build();
    }

    private ContractDefinition updateAContractDefinitionRequest(String id) {
        var assetsSelector = JsonLdCriterion.Builder.newInstance()
                .operandLeft("spatial")
                .operator("=")
                .operandRight("https://www.wikidata.org/wiki/Q183")
                .build();
        return JsonLdContractDefinition.Builder.newInstance()
                .id(id)
                .accessPolicyId("asset-policy-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(List.of(assetsSelector))
                .build();
    }

    private QuerySpec getContractDefinitionsQuery() {
        return JsonLdQuerySpec.Builder.newInstance()
                .limit(10)
                .sortOrder("DESC")
                .filterExpression(emptyList())
                .build();
    }
}
