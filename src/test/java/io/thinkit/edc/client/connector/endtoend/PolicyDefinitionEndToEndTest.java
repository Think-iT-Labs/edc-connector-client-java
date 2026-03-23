package io.thinkit.edc.client.connector.endtoend;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static jakarta.json.Json.createObjectBuilder;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.RealTimeConnectorApiTestBase;
import io.thinkit.edc.client.connector.model.PolicyDefinition;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.*;
import io.thinkit.edc.client.connector.services.management.PolicyDefinitions;
import jakarta.json.Json;
import java.net.http.HttpClient;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;
//
// @ParameterizedClass
// @ValueSource(strings = {V3, V4BETA})
// class PolicyDefinitionEndToEndTest extends RealTimeConnectorApiTestBase {
//
//    private final HttpClient http = HttpClient.newBuilder().build();
//    private final String managementVersion;
//    private PolicyDefinitions policyDefinitions;
//
//    PolicyDefinitionEndToEndTest(String managementVersion) {
//        this.managementVersion = managementVersion;
//    }
//
//    @BeforeEach
//    void setUp() {
//        var client = EdcConnectorClient.newBuilder()
//                .httpClient(http)
//                .management(getManagementUrl(), managementVersion)
//                .build();
//        policyDefinitions = client.policyDefinitions();
//    }
//
//    @Nested
//    class Sync {
//
//        @Test
//        void should_create_a_policy_definition() {
//            var id = "policyId-" + UUID.randomUUID();
//
//            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
//
//            assertThat(created.isSucceeded()).isTrue();
//            assertThat(created.getContent()).isNotNull();
//            assertThat(created.getContent()).isEqualTo(id);
//        }
//
//        @Test
//        void should_fail_creating_two_policy_definitions_with_the_same_id() {
//            var id = "policyId-" + UUID.randomUUID();
//            var firstCreate = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
//            assertThat(firstCreate.isSucceeded()).isTrue();
//
//            var secondCreate = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
//
//            assertThat(secondCreate.isSucceeded()).isFalse();
//            assertThat(secondCreate.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
//                assertThat(apiErrorDetail.type()).isEqualTo("DuplicateResource");
//            });
//        }
//
//        @Test
//        void should_get_a_policy_definition() {
//            var id = "policyId-" + UUID.randomUUID();
//            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
//
//            var policyDefinition = policyDefinitions.get(created.getContent());
//
//            assertThat(policyDefinition.isSucceeded()).isTrue();
//            assertThat(policyDefinition.getContent()).isNotNull();
//            assertThat(policyDefinition.getContent().id()).isEqualTo(created.getContent());
//        }
//
//        @Test
//        void should_fail_to_get_a_non_existent_policy_definition() {
//            var policyDefinition = policyDefinitions.get("non-existent-" + UUID.randomUUID());
//
//            assertThat(policyDefinition.isSucceeded()).isFalse();
//            assertThat(policyDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
//                assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
//            });
//        }
//
//        @Test
//        void should_query_all_policy_definitions() {
//            var id = "policyId-" + UUID.randomUUID();
//            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
//
//            var policyDefinitionList = policyDefinitions.request(shouldGetPolicyDefinitionsQuery());
//
//            assertThat(policyDefinitionList.isSucceeded()).isTrue();
//            assertThat(policyDefinitionList.getContent())
//                    .anyMatch(policy -> policy.id().equals(created.getContent()));
//        }
//
//        //        @Test
//        //        void should_update_a_policy_definition() {
//        //            var id = "policyId-" + UUID.randomUUID();
//        //            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
//        //            assertThat(created.isSucceeded()).isTrue();
//        //
//        //            var updated = policyDefinitions.update(shouldUpdateAPolicyDefinitionRequest(id));
//        //
//        //            assertThat(updated.isSucceeded()).isTrue();
//        //        }
//
//        //        @Test
//        //        void should_fail_to_update_a_non_existent_policy_definition() {
//        //            var nonExistentId = "non-existent-" + UUID.randomUUID();
//        //            var updated = policyDefinitions.update(shouldUpdateAPolicyDefinitionRequest(nonExistentId));
//        //
//        //            assertThat(updated.isSucceeded()).isFalse();
//        //            assertThat(updated.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
//        //                assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
//        //            });
//        //        }
//
//        @Test
//        void should_delete_a_policy_definition() {
//            var id = "policyId-" + UUID.randomUUID();
//            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
//
//            var deleted = policyDefinitions.delete(created.getContent());
//            var policyDefinition = policyDefinitions.get(created.getContent());
//
//            assertThat(deleted.isSucceeded()).isTrue();
//            assertThat(policyDefinition.isSucceeded()).isFalse();
//            assertThat(policyDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
//                assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
//            });
//        }
//
//        @Test
//        void should_fail_to_delete_a_non_existent_policy_definition() {
//            var deleted = policyDefinitions.delete("non-existent-" + UUID.randomUUID());
//
//            assertThat(deleted.isSucceeded()).isFalse();
//            assertThat(deleted.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
//                assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
//            });
//        }
//    }
//
//    // --- Request Builders ---
//
//    private PolicyDefinition shouldCreateAPolicyDefinitionRequest(String id) {
//        var constraints = Json.createArrayBuilder()
//                .add(createObjectBuilder()
//                        .add("leftOperand", "spatial")
//                        .add("operator", "eq")
//                        .add("rightOperand", "https://www.wikidata.org/wiki/Q183")
//                        .add("comment", "i.e Germany"))
//                .build();
//
//        var permissions = Json.createArrayBuilder()
//                .add(createObjectBuilder()
//                        .add("target", "http://example.com/asset:9898.movie")
//                        .add("action", "display")
//                        .add("constraints", constraints))
//                .build();
//
//        var policy = JsonLdPolicy.Builder.newInstance()
//                .raw(createObjectBuilder().add("permission", permissions).build())
//                .build();
//
//        return JsonLdPolicyDefinition.Builder.newInstance()
//                .id(id)
//                .policy(policy)
//                .build();
//    }
//
//    private QuerySpec shouldGetPolicyDefinitionsQuery() {
//        return JsonLdQuerySpec.Builder.newInstance()
//                .limit(10)
//                .sortOrder("DESC")
//                .filterExpression(emptyList())
//                .build();
//    }
// }

@Nested
@ParameterizedClass
@ValueSource(strings = {V3, V4BETA})
class PolicyDefinitionEndToEndTest extends RealTimeConnectorApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private PolicyDefinitions policyDefinitions;

    PolicyDefinitionEndToEndTest(String managementVersion) {
        this.managementVersion = managementVersion;
    }

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(getManagementUrl(), managementVersion)
                .build();
        policyDefinitions = client.policyDefinitions();
    }

    @Nested
    class Sync {

        @Test
        void should_create_a_policy_definition() {
            var id = "policyId-" + UUID.randomUUID();

            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
            assertThat(created.getContent()).isEqualTo(id);
        }

        @Test
        void should_fail_creating_two_policy_definitions_with_the_same_id() {
            var id = "policyId-" + UUID.randomUUID();
            var firstCreate = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
            assertThat(firstCreate.isSucceeded()).isTrue();

            var secondCreate = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));

            assertThat(secondCreate.isSucceeded()).isFalse();
            assertThat(secondCreate.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.type()).isEqualTo("ObjectConflict");
            });
        }

        @Test
        void should_get_a_policy_definition() {
            var id = "policyId-" + UUID.randomUUID();
            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));

            var policyDefinition = policyDefinitions.get(created.getContent());

            assertThat(policyDefinition.getContent()).isNotNull();
            assertThat(policyDefinition.getContent().id()).isEqualTo(created.getContent());
            assertThat(policyDefinition.getContent().policy()).isNotNull();
        }

        @Test
        void should_fail_to_get_a_non_existent_policy_definition() {
            var policyDefinition = policyDefinitions.get("non-existent-" + UUID.randomUUID());

            assertThat(policyDefinition.isSucceeded()).isFalse();
            assertThat(policyDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
            });
        }

        @Test
        void should_query_all_policy_definitions() {
            var id = "policyId-" + UUID.randomUUID();
            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));

            var policyDefinitionList = policyDefinitions.request(shouldGetPolicyDefinitionsQuery());

            assertThat(policyDefinitionList.isSucceeded()).isTrue();
            assertThat(policyDefinitionList.getContent())
                    .anyMatch(policy -> policy.id().equals(created.getContent()));
        }

        // TODO : finish update tests
        @Test
        void should_update_a_policy_definition() {
            var id = "policyId-" + UUID.randomUUID();
            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));
            assertThat(created.isSucceeded()).isTrue();

            var updated = policyDefinitions.update(shouldUpdateAPolicyDefinitionRequest(id));

            assertThat(updated.isSucceeded()).isTrue();
            var fetched = policyDefinitions.get(id);

            assertThat(fetched.getContent()).isNotNull();

            var policy = fetched.getContent().policy();
            var permissions = policy.permissions();

            ///  check on the values changed
        }
        // TODO : finish update tests
        //                @Test
        //                void should_fail_to_update_a_non_existent_policy_definition() {
        //                    var nonExistentId = "non-existent-" + UUID.randomUUID();
        //                    var updated =
        // policyDefinitions.update(shouldUpdateAPolicyDefinitionRequest(nonExistentId));
        //
        //                    assertThat(updated.isSucceeded()).isFalse();
        //                    assertThat(updated.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
        //                        assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
        //                    });
        //                }

        @Test
        void should_delete_a_policy_definition() {
            var id = "policyId-" + UUID.randomUUID();
            var created = policyDefinitions.create(shouldCreateAPolicyDefinitionRequest(id));

            var deleted = policyDefinitions.delete(created.getContent());
            var policyDefinition = policyDefinitions.get(created.getContent());

            assertThat(deleted.isSucceeded()).isTrue();
            assertThat(policyDefinition.isSucceeded()).isFalse();
            assertThat(policyDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
            });
        }

        @Test
        void should_fail_to_delete_a_non_existent_policy_definition() {
            var deleted = policyDefinitions.delete("non-existent-" + UUID.randomUUID());

            assertThat(deleted.isSucceeded()).isFalse();
            assertThat(deleted.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
            });
        }
    }

    private PolicyDefinition shouldCreateAPolicyDefinitionRequest(String id) {
        var constraints = Json.createArrayBuilder()
                .add(createObjectBuilder()
                        .add("leftOperand", "https://www.wikidata.org/wiki/Q183") // use a real IRI
                        .add("operator", "eq")
                        .add("rightOperand", "https://www.wikidata.org/wiki/Q183"))
                .build();

        var permissions = Json.createArrayBuilder()
                .add(createObjectBuilder()
                        .add("target", "http://example.com/asset:9898.movie")
                        .add("action", "use")
                        .add("constraint", constraints))
                .build();

        var policy = JsonLdPolicy.Builder.newInstance()
                .raw(createObjectBuilder().add("permission", permissions).build())
                .build();

        return JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policy)
                .build();
    }

    private PolicyDefinition shouldUpdateAPolicyDefinitionRequest(String id) {

        var permissions = Json.createArrayBuilder()
                .add(createObjectBuilder()
                        .add("target", "http://example.com/asset:9898.movie/test") // change
                        .add("action", "use"))
                .build();

        var policy = JsonLdPolicy.Builder.newInstance()
                .raw(createObjectBuilder().add("permission", permissions).build())
                .build();

        return JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policy)
                .build();
    }

    private QuerySpec shouldGetPolicyDefinitionsQuery() {
        return JsonLdQuerySpec.Builder.newInstance()
                .limit(10)
                .sortOrder("DESC")
                .filterExpression(emptyList())
                .build();
    }

    private void shouldGetAPolicyDefinitionResponse(Result<PolicyDefinition> policyDefinition) {
        assertThat(policyDefinition.isSucceeded()).isTrue();
        assertThat(policyDefinition.getContent().policy()).isNotNull();
        assertThat(policyDefinition.getContent().createdAt()).isGreaterThan(-1);
    }
}
