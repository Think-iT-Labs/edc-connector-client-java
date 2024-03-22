package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createObjectBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.Policy;
import io.thinkit.edc.client.connector.model.PolicyDefinition;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.services.PolicyDefinitions;
import jakarta.json.Json;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PolicyDefinitionsTest extends ContainerTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private PolicyDefinitions policyDefinitions;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        policyDefinitions = client.policyDefinitions();
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

    void should_get_a_policy_definition_response(Result<PolicyDefinition> policyDefinition) {
        assertThat(policyDefinition.isSucceeded()).isTrue();
        assertThat(policyDefinition.getContent().id()).isNotBlank();
        assertThat(policyDefinition.getContent().policy()).isNotNull().satisfies(policy -> assertThat(
                        policy.getList(ODRL_NAMESPACE + "permission").size())
                .isGreaterThan(0));
        assertThat(policyDefinition.getContent().createdAt()).isGreaterThan(0);
    }

    PolicyDefinition should_create_a_policy_definition_request() {
        var constraints = Json.createArrayBuilder()
                .add(createObjectBuilder()
                        .add("leftOperand", "spatial")
                        .add("operator", "eq")
                        .add("rightOperand", "https://www.wikidata.org/wiki/Q183")
                        .add("comment", "i.e Germany"))
                .build();
        var permissions = Json.createArrayBuilder()
                .add(createObjectBuilder()
                        .add("target", "http://example.com/asset:9898.movie")
                        .add("action", "display")
                        .add("constraints", constraints))
                .build();

        var policy = Policy.Builder.newInstance()
                .raw(createObjectBuilder().add("permission", permissions).build())
                .build();

        return PolicyDefinition.Builder.newInstance()
                .id("definition-id")
                .policy(policy)
                .build();
    }

    QuerySpec should_get_policy_definitions_request() {
        return QuerySpec.Builder.newInstance()
                .offset(0)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
    }

    void should_get_policy_definitions_response(Result<List<PolicyDefinition>> PolicyDefinitionList) {
        assertThat(PolicyDefinitionList.isSucceeded()).isTrue();
        assertThat(PolicyDefinitionList.getContent()).isNotNull().first().satisfies(policyDefinition -> {
            assertThat(policyDefinition.id()).isNotBlank();
            assertThat(policyDefinition.policy()).isNotNull().satisfies(policy -> assertThat(
                            policy.getList(ODRL_NAMESPACE + "permission").size())
                    .isGreaterThan(0));
            assertThat(policyDefinition.createdAt()).isGreaterThan(0);
        });
    }

    @Nested
    class Sync {
        @Test
        void should_get_a_policy_definition() {
            Result<PolicyDefinition> policyDefinition = policyDefinitions.get("definition-id");
            should_get_a_policy_definition_response(policyDefinition);
        }

        @Test
        void should_not_get_a_policy_definition_when_id_is_empty() {
            Result<PolicyDefinition> policyDefinition = policyDefinitions.get("");
            error_response(policyDefinition);
        }

        @Test
        void should_create_a_policy_definition() {

            var created = policyDefinitions.create(should_create_a_policy_definition_request());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_not_create_a_policy_definition() {

            var policyDefinition =
                    PolicyDefinition.Builder.newInstance().id("definition-id").build();

            var created = policyDefinitions.create(policyDefinition);
            error_response(created);
        }

        @Test
        void should_update_a_policy_definition() {
            var updated = policyDefinitions.update(should_create_a_policy_definition_request());

            assertThat(updated.isSucceeded()).isTrue();
        }

        @Test
        void should_not_update_a_policy_definition() {

            var policyDefinition =
                    PolicyDefinition.Builder.newInstance().id("definition-id").build();

            var updated = policyDefinitions.update(policyDefinition);

            error_response(updated);
        }

        @Test
        void should_delete_a_policy_definition() {
            var deleted = policyDefinitions.delete("definition-id");

            assertThat(deleted.isSucceeded()).isTrue();
        }

        @Test
        void should_not_delete_a_policy_definition_when_id_is_empty() {
            var deleted = policyDefinitions.delete("");

            error_response(deleted);
        }

        @Test
        void should_get_policy_definitions() {

            var PolicyDefinitionList = policyDefinitions.request(should_get_policy_definitions_request());
            should_get_policy_definitions_response(PolicyDefinitionList);
        }

        @Test
        void should_not_get_policy_definitions() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();
            var result = policyDefinitions.request(input);
            error_response(result);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_policy_definition_async() {
            var policyDefinition = policyDefinitions.getAsync("definition-id");
            assertThat(policyDefinition)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(PolicyDefinitionsTest.this::should_get_a_policy_definition_response);
        }

        @Test
        void should_not_get_a_policy_definition_when_id_is_empty_async() {
            var policyDefinition = policyDefinitions.getAsync("");

            assertThat(policyDefinition)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(PolicyDefinitionsTest.this::error_response);
        }

        @Test
        void should_create_a_policy_definition_async() {
            var result = policyDefinitions.createAsync(should_create_a_policy_definition_request());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_create_a_policy_definition_async() {
            var policyDefinition =
                    PolicyDefinition.Builder.newInstance().id("definition-id").build();

            var created = policyDefinitions.createAsync(policyDefinition);

            assertThat(created)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(PolicyDefinitionsTest.this::error_response);
        }

        @Test
        void should_update_a_policy_definition_async() {

            var result = policyDefinitions.updateAsync(should_create_a_policy_definition_request());
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(updated -> assertThat(updated.isSucceeded()).isTrue());
        }

        @Test
        void should_not_update_a_policy_definition_async() {
            var policyDefinition =
                    PolicyDefinition.Builder.newInstance().id("definition-id").build();

            var updated = policyDefinitions.updateAsync(policyDefinition);
            assertThat(updated)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(PolicyDefinitionsTest.this::error_response);
        }

        @Test
        void should_delete_a_policy_definition_async() {
            var result = policyDefinitions.deleteAsync("definition-id");
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(deleted -> assertThat(deleted.isSucceeded()).isTrue());
        }

        @Test
        void should_not_delete_a_policy_definition_when_id_is_empty_async() {
            var deleted = policyDefinitions.deleteAsync("");

            assertThat(deleted)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(PolicyDefinitionsTest.this::error_response);
        }

        @Test
        void should_get_policy_definitions_async() {

            var PolicyDefinitionList = policyDefinitions.requestAsync(should_get_policy_definitions_request());
            assertThat(PolicyDefinitionList)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(PolicyDefinitionsTest.this::should_get_policy_definitions_response);
        }

        @Test
        void should_not_get_policy_definitions_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = policyDefinitions.requestAsync(input);

            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(PolicyDefinitionsTest.this::error_response);
        }
    }
}
