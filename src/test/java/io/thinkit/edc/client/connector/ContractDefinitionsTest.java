package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class ContractDefinitionsTest {

    @Container
    private ManagementApiContainer prism = new ManagementApiContainer();

    private final HttpClient http = HttpClient.newBuilder().build();
    private ContractDefinitions contractDefinitions;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .interceptor(r -> r.header("Prefer", "dynamic=false"))
                .managementUrl("http://127.0.0.1:%s".formatted(prism.getFirstMappedPort()))
                .build();
        contractDefinitions = client.contractDefinitions();
    }

    @Test
    void should_get_a_contract_definition() {
        Result<ContractDefinition> contractDefinition = contractDefinitions.get("definition-id");

        assertThat(contractDefinition.isSucceeded()).isTrue();
        assertThat(contractDefinition.getContent().id()).isNotBlank();
        assertThat(contractDefinition.getContent().accessPolicyId()).isNotNull().satisfies(accessPolicyId -> {
            assertThat(accessPolicyId).isEqualTo("asset-policy-id");
        });
        assertThat(contractDefinition.getContent().contractPolicyId())
                .isNotNull()
                .satisfies(contractPolicyId -> {
                    assertThat(contractPolicyId).isEqualTo("contract-policy-id");
                });
        assertThat(contractDefinition.getContent().createdAt()).isGreaterThan(0);
    }

    @Test
    void should_not_get_a_contract_definition_when_id_is_empty() {
        Result<ContractDefinition> contractDefinition = contractDefinitions.get("");

        assertThat(contractDefinition.isSucceeded()).isFalse();
        assertThat(contractDefinition.getError()).isNotNull();
    }

    @Test
    void should_create_a_contract_definition() {
        var contractDefinitionInput = new ContractDefinitionInput(
                "definition-id", "asset-policy-id", "contract-policy-id", new ArrayList<CriterionInput>());

        Result<String> created = contractDefinitions.create(contractDefinitionInput);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should__not_create_a_contract_definition_when_id_is_null() {
        var contractDefinitionInput = new ContractDefinitionInput(
                null, "asset-policy-id", "contract-policy-id", new ArrayList<CriterionInput>());

        Result<String> created = contractDefinitions.create(contractDefinitionInput);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getError()).isNotNull();
    }
}
