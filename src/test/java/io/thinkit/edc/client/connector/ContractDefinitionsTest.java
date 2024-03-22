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

    <T> void error_response(Result<T> error) {
        assertThat(error.isSucceeded()).isFalse();
        assertThat(error.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    void should_get_a_contract_definition_response(Result<ContractDefinition> contractDefinition) {
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

    ContractDefinition should_create_a_contract_definition_request() {
        return ContractDefinition.Builder.newInstance()
                .id("definition-id")
                .accessPolicyId("asset-policy-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(emptyList())
                .build();
    }

    ContractDefinition should_not_create_a_contract_definition_request() {
        return ContractDefinition.Builder.newInstance()
                .id("definition-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(emptyList())
                .build();
    }

    QuerySpec should_get_contract_definitions_query() {
        return QuerySpec.Builder.newInstance()
                .offset(0)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
    }

    void should_get_contract_definitions_response(Result<List<ContractDefinition>> ContractDefinitionList) {
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

    @Nested
    class Sync {
        @Test
        void should_get_a_contract_definition() {
            var contractDefinition = contractDefinitions.get("definition-id");
            should_get_a_contract_definition_response(contractDefinition);
        }

        @Test
        void should_not_get_a_contract_definition_when_id_is_empty() {
            var contractDefinition = contractDefinitions.get("");
            error_response(contractDefinition);
        }

        @Test
        void should_create_a_contract_definition() {
            var created = contractDefinitions.create(should_create_a_contract_definition_request());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should__not_create_a_contract_definition_when_id_is_null() {
            var created = contractDefinitions.create(should_not_create_a_contract_definition_request());
            error_response(created);
        }

        @Test
        void should_delete_a_contract_definition() {

            var deleted = contractDefinitions.delete("definition-id");

            assertThat(deleted.isSucceeded()).isTrue();
        }

        @Test
        void should_not_delete_a_contract_definition_when_id_is_empty() {
            var deleted = contractDefinitions.delete("");
            error_response((deleted));
        }

        @Test
        void should_get_contract_definitions() {

            var ContractDefinitionList = contractDefinitions.request(should_get_contract_definitions_query());
            should_get_contract_definitions_response(ContractDefinitionList);
        }

        @Test
        void should_not_get_contract_definitions() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = contractDefinitions.request(input);
            error_response(result);
        }

        @Test
        void should_update_a_contract_definition() {
            var created = contractDefinitions.update(should_create_a_contract_definition_request());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_not_update_a_contract_definition_when_id_is_empty() {
            var updated = contractDefinitions.update(should_not_create_a_contract_definition_request());
            error_response(updated);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_contract_definition_async() {
            var contractDefinition = contractDefinitions.getAsync("definition-id");
            assertThat(contractDefinition)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::should_get_a_contract_definition_response);
        }

        @Test
        void should_not_get_a_contract_definition_when_id_is_empty_async() {
            var contractDefinition = contractDefinitions.getAsync("");
            assertThat(contractDefinition)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::error_response);
        }

        @Test
        void should_create_a_contract_definition_async() {
            var result = contractDefinitions.createAsync(should_create_a_contract_definition_request());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should__not_create_a_contract_definition_when_id_is_null_async() {

            var created = contractDefinitions.createAsync(should_not_create_a_contract_definition_request());
            assertThat(created)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::error_response);
        }

        @Test
        void should_delete_a_contract_definition_async() {

            var result = contractDefinitions.deleteAsync("definition-id");
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(deleted -> {
                assertThat(deleted.isSucceeded()).isTrue();
            });
        }

        @Test
        void should_not_delete_a_contract_definition_when_id_is_empty_async() {
            var deleted = contractDefinitions.deleteAsync("");
            assertThat(deleted)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::error_response);
        }

        @Test
        void should_get_contract_definitions_async() {
            var ContractDefinitionList = contractDefinitions.requestAsync(should_get_contract_definitions_query());
            assertThat(ContractDefinitionList)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::should_get_contract_definitions_response);
        }

        @Test
        void should_not_get_contract_definitions_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();
            var result = contractDefinitions.requestAsync(input);
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::error_response);
        }

        @Test
        void should_update_a_contract_definition_async() {
            var result = contractDefinitions.updateAsync(should_create_a_contract_definition_request());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(updated -> {
                assertThat(updated.isSucceeded()).isTrue();
                assertThat(updated.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_update_a_contract_definition_when_id_is_empty_async() {

            var result = contractDefinitions.updateAsync(should_not_create_a_contract_definition_request());
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractDefinitionsTest.this::error_response);
        }
    }
}
