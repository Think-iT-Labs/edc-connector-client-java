package io.thinkit.edc.client.connector;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.ContractDefinition;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.services.ContractDefinitions;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void should_get_a_contract_definition() {
        var contractDefinition = contractDefinitions.get("definition-id");

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

    @Test
    void should_get_a_contract_definition_async() {
        try {
            Result<ContractDefinition> contractDefinition =
                    contractDefinitions.getAsync("definition-id").get();

            assertThat(contractDefinition.isSucceeded()).isTrue();
            assertThat(contractDefinition.getContent().id()).isNotBlank();
            assertThat(contractDefinition.getContent().accessPolicyId())
                    .isNotNull()
                    .satisfies(accessPolicyId -> assertThat(accessPolicyId).isEqualTo("asset-policy-id"));
            assertThat(contractDefinition.getContent().contractPolicyId())
                    .isNotNull()
                    .satisfies(contractPolicyId -> assertThat(contractPolicyId).isEqualTo("contract-policy-id"));
            assertThat(contractDefinition.getContent().createdAt()).isGreaterThan(0);

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_get_a_contract_definition_when_id_is_empty() {
        var contractDefinition = contractDefinitions.get("");

        assertThat(contractDefinition.isSucceeded()).isFalse();
        assertThat(contractDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_not_get_a_contract_definition_when_id_is_empty_async() {
        try {
            Result<ContractDefinition> contractDefinition =
                    contractDefinitions.getAsync("").get();
            assertThat(contractDefinition.isSucceeded()).isFalse();
            assertThat(contractDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_create_a_contract_definition() {
        var contractDefinitionInput = ContractDefinition.Builder.newInstance()
                .id("definition-id")
                .accessPolicyId("asset-policy-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(emptyList())
                .build();
        var created = contractDefinitions.create(contractDefinitionInput);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should_create_a_contract_definition_async() {

        try {
            var contractDefinitionInput = ContractDefinition.Builder.newInstance()
                    .id("definition-id")
                    .accessPolicyId("asset-policy-id")
                    .contractPolicyId("contract-policy-id")
                    .assetsSelector(emptyList())
                    .build();
            Result<String> created =
                    contractDefinitions.createAsync(contractDefinitionInput).get();
            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should__not_create_a_contract_definition_when_id_is_null() {
        var contractDefinitionInput = ContractDefinition.Builder.newInstance()
                .id("definition-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(emptyList())
                .build();
        var created = contractDefinitions.create(contractDefinitionInput);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should__not_create_a_contract_definition_when_id_is_null_async() {

        try {
            var contractDefinitionInput = ContractDefinition.Builder.newInstance()
                    .id("definition-id")
                    .contractPolicyId("contract-policy-id")
                    .assetsSelector(emptyList())
                    .build();
            Result<String> created =
                    contractDefinitions.createAsync(contractDefinitionInput).get();
            assertThat(created.isSucceeded()).isFalse();
            assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_delete_a_contract_definition() {

        var deleted = contractDefinitions.delete("definition-id");

        assertThat(deleted.isSucceeded()).isTrue();
    }

    @Test
    void should_delete_a_contract_definition_async() {

        try {
            Result<String> deleted =
                    contractDefinitions.deleteAsync("definition-id").get();
            assertThat(deleted.isSucceeded()).isTrue();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_delete_a_contract_definition_when_id_is_empty() {
        var deleted = contractDefinitions.delete("");

        assertThat(deleted.isSucceeded()).isFalse();
        assertThat(deleted.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_not_delete_a_contract_definition_when_id_is_empty_async() {
        try {
            var deleted = contractDefinitions.deleteAsync("").get();
            assertThat(deleted.isSucceeded()).isFalse();
            assertThat(deleted.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_get_contract_definitions() {
        var input = QuerySpec.Builder.newInstance()
                .offset(0)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();

        var ContractDefinitionList = contractDefinitions.request(input);

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

    @Test
    void should_get_contract_definitions_async() {
        try {
            var input = QuerySpec.Builder.newInstance()
                    .offset(0)
                    .limit(10)
                    .sortOrder("DESC")
                    .sortField("fieldName")
                    .build();
            var ContractDefinitionList = contractDefinitions.requestAsync(input).get();
            assertThat(ContractDefinitionList.isSucceeded()).isTrue();
            assertThat(ContractDefinitionList.getContent()).isNotNull().first().satisfies(contractDefinition -> {
                assertThat(contractDefinition.id()).isNotBlank();
                assertThat(contractDefinition.accessPolicyId())
                        .isNotNull()
                        .satisfies(accessPolicyId -> assertThat(accessPolicyId).isEqualTo("asset-policy-id"));
                assertThat(contractDefinition.contractPolicyId()).isNotNull().satisfies(contractPolicyId -> assertThat(
                                contractPolicyId)
                        .isEqualTo("contract-policy-id"));
                assertThat(contractDefinition.createdAt()).isGreaterThan(0);
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_get_contract_definitions() {
        var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

        var result = contractDefinitions.request(input);

        assertThat(result.isSucceeded()).isFalse();
        assertThat(result.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_not_get_contract_definitions_async() {
        try {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();
            var result = contractDefinitions.requestAsync(input).get();

            assertThat(result.isSucceeded()).isFalse();
            assertThat(result.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_update_a_contract_definition() {
        var contractDefinitionInput = ContractDefinition.Builder.newInstance()
                .id("definition-id")
                .accessPolicyId("asset-policy-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(emptyList())
                .build();
        var created = contractDefinitions.update(contractDefinitionInput);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should_update_a_contract_definition_async() {
        try {
            var contractDefinitionInput = ContractDefinition.Builder.newInstance()
                    .id("definition-id")
                    .accessPolicyId("asset-policy-id")
                    .contractPolicyId("contract-policy-id")
                    .assetsSelector(emptyList())
                    .build();
            var created =
                    contractDefinitions.updateAsync(contractDefinitionInput).get();

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_update_a_contract_definition_when_id_is_empty() {
        var contractDefinitionInput = ContractDefinition.Builder.newInstance()
                .id("definition-id")
                .contractPolicyId("contract-policy-id")
                .assetsSelector(emptyList())
                .build();
        var created = contractDefinitions.update(contractDefinitionInput);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_not_update_a_contract_definition_when_id_is_empty_async() {

        try {
            var contractDefinitionInput = ContractDefinition.Builder.newInstance()
                    .id("definition-id")
                    .contractPolicyId("contract-policy-id")
                    .assetsSelector(emptyList())
                    .build();
            var created =
                    contractDefinitions.updateAsync(contractDefinitionInput).get();
            assertThat(created.isSucceeded()).isFalse();
            assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
