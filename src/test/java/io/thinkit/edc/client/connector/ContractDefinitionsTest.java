package io.thinkit.edc.client.connector;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.ContractDefinition;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.services.ContractDefinitions;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ContractDefinitionsTest extends ContainerTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private ContractDefinitions contractDefinitions;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        contractDefinitions = client.contractDefinitions();
    }

    @Nested
    class Sync {
        @Test
        void should_get_a_contract_definition() {
            var contractDefinition = contractDefinitions.get("definition-id");
            assertThat(contractDefinition)
                    .satisfies(ContractDefinitionsTest.this::shouldGetAContractDefinitionResponse);
        }

        @Test
        void should_not_get_a_contract_definition_when_id_is_empty() {
            var contractDefinition = contractDefinitions.get("");
            assertThat(contractDefinition).satisfies(ContractDefinitionsTest.this::errorResponse);
        }

        @Test
        void should_create_a_contract_definition() {
            var created = contractDefinitions.create(shouldCreateAContractDefinitionRequest());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should__not_create_a_contract_definition_when_id_is_null() {
            var created = contractDefinitions.create(shouldNotCreateAContractDefinitionRequest());
            assertThat(created).satisfies(ContractDefinitionsTest.this::errorResponse);
        }

        @Test
        void should_delete_a_contract_definition() {

            var deleted = contractDefinitions.delete("definition-id");

            assertThat(deleted.isSucceeded()).isTrue();
        }

        @Test
        void should_not_delete_a_contract_definition_when_id_is_empty() {
            var deleted = contractDefinitions.delete("");
            assertThat(deleted).satisfies(ContractDefinitionsTest.this::errorResponse);
        }

        @Test
        void should_get_contract_definitions() {

            var ContractDefinitionList = contractDefinitions.request(shouldGetContractDefinitionsQuery());
            assertThat(ContractDefinitionList)
                    .satisfies(ContractDefinitionsTest.this::shouldGetContractDefinitionsResponse);
        }

        @Test
        void should_not_get_contract_definitions() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = contractDefinitions.request(input);
            assertThat(result).satisfies(ContractDefinitionsTest.this::errorResponse);
        }

        @Test
        void should_update_a_contract_definition() {
            var created = contractDefinitions.update(shouldCreateAContractDefinitionRequest());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_not_update_a_contract_definition_when_id_is_empty() {
            var updated = contractDefinitions.update(shouldNotCreateAContractDefinitionRequest());
            assertThat(updated).satisfies(ContractDefinitionsTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_contract_definition_async() {
            var contractDefinition = contractDefinitions.getAsync("definition-id");
            assertThat(contractDefinition)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::shouldGetAContractDefinitionResponse);
        }

        @Test
        void should_not_get_a_contract_definition_when_id_is_empty_async() {
            var contractDefinition = contractDefinitions.getAsync("");
            assertThat(contractDefinition)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::errorResponse);
        }

        @Test
        void should_create_a_contract_definition_async() {
            var result = contractDefinitions.createAsync(shouldCreateAContractDefinitionRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should__not_create_a_contract_definition_when_id_is_null_async() {

            var created = contractDefinitions.createAsync(shouldNotCreateAContractDefinitionRequest());
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::errorResponse);
        }

        @Test
        void should_delete_a_contract_definition_async() {

            var result = contractDefinitions.deleteAsync("definition-id");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_not_delete_a_contract_definition_when_id_is_empty_async() {
            var deleted = contractDefinitions.deleteAsync("");
            assertThat(deleted)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::errorResponse);
        }

        @Test
        void should_get_contract_definitions_async() {
            var ContractDefinitionList = contractDefinitions.requestAsync(shouldGetContractDefinitionsQuery());
            assertThat(ContractDefinitionList)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::shouldGetContractDefinitionsResponse);
        }

        @Test
        void should_not_get_contract_definitions_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();
            var result = contractDefinitions.requestAsync(input);
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::errorResponse);
        }

        @Test
        void should_update_a_contract_definition_async() {
            var result = contractDefinitions.updateAsync(shouldCreateAContractDefinitionRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(updated -> {
                assertThat(updated.isSucceeded()).isTrue();
                assertThat(updated.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_update_a_contract_definition_when_id_is_empty_async() {

            var result = contractDefinitions.updateAsync(shouldNotCreateAContractDefinitionRequest());
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::errorResponse);
        }
    }

    private <T> void errorResponse(Result<T> error) {
        assertThat(error.isSucceeded()).isFalse();
        assertThat(error.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    private void shouldGetAContractDefinitionResponse(Result<ContractDefinition> contractDefinition) {
        assertThat(contractDefinition.isSucceeded()).isTrue();
        assertThat(contractDefinition.getContent().id()).isNotBlank();
        assertThat(contractDefinition.getContent().accessPolicyId())
                .isNotNull()
                .satisfies(accessPolicyId -> assertThat(accessPolicyId).isEqualTo("asset-policy-id"));
        assertThat(contractDefinition.getContent().contractPolicyId())
                .isNotNull()
                .satisfies(contractPolicyId -> assertThat(contractPolicyId).isEqualTo("contract-policy-id"));
        assertThat(contractDefinition.getContent().createdAt()).isGreaterThan(0);
    }

    private ContractDefinition shouldCreateAContractDefinitionRequest() {
        return ContractDefinition.Builder.newInstance()
                .id("definition-id")
                .accessPolicyId("asset-policy-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(emptyList())
                .build();
    }

    private ContractDefinition shouldNotCreateAContractDefinitionRequest() {
        return ContractDefinition.Builder.newInstance()
                .id("definition-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(emptyList())
                .build();
    }

    private QuerySpec shouldGetContractDefinitionsQuery() {
        return QuerySpec.Builder.newInstance()
                .offset(0)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
    }

    private void shouldGetContractDefinitionsResponse(Result<List<ContractDefinition>> ContractDefinitionList) {
        assertThat(ContractDefinitionList.isSucceeded()).isTrue();
        assertThat(ContractDefinitionList.getContent()).isNotNull().first().satisfies(contractDefinition -> {
            assertThat(contractDefinition.id()).isNotBlank();
            assertThat(contractDefinition.accessPolicyId())
                    .isNotNull()
                    .satisfies(accessPolicyId -> assertThat(accessPolicyId).isEqualTo("asset-policy-id"));
            assertThat(contractDefinition.contractPolicyId())
                    .isNotNull()
                    .satisfies(contractPolicyId -> assertThat(contractPolicyId).isEqualTo("contract-policy-id"));
            assertThat(contractDefinition.createdAt()).isGreaterThan(0);
        });
    }
}
