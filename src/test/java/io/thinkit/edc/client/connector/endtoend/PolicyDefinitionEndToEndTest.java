package io.thinkit.edc.client.connector.endtoend;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static io.thinkit.edc.client.connector.utils.Constants.ODRL_NAMESPACE;
import static jakarta.json.Json.createObjectBuilder;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.RealTimeConnectorApiTestBase;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.jsonld.*;
import io.thinkit.edc.client.connector.services.management.PolicyDefinitions;
import jakarta.json.Json;
import java.net.http.HttpClient;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

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
                .management(getProviderManagementUrl(), managementVersion)
                .build();
        policyDefinitions = client.policyDefinitions();
    }

    @Test
    void should_create_a_policy_definition() {
        var id = "policyId-" + UUID.randomUUID();

        var policyDefinition = JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policyWithPermission())
                .build();

        var created = policyDefinitions.create(policyDefinition);

        var fetched = policyDefinitions.get(id);

        assertThat(fetched.getContent().id()).isEqualTo(id);
        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isEqualTo(id);
    }

    @Test
    void should_fail_creating_two_policy_definitions_with_the_same_id() {
        var id = "policyId-" + UUID.randomUUID();
        var policyDefinition = JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policyWithPermission())
                .build();

        var firstCreate = policyDefinitions.create(policyDefinition);
        assertThat(firstCreate.isSucceeded()).isTrue();

        var secondCreate = policyDefinitions.create(policyDefinition);

        assertThat(secondCreate.isSucceeded()).isFalse();
        assertThat(secondCreate.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.type()).isEqualTo("ObjectConflict");
        });
    }

    @Test
    void should_get_a_policy_definition() {
        var id = "policyId-" + UUID.randomUUID();

        var policyDefinition = JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policyWithPermission())
                .build();
        var created = policyDefinitions.create(policyDefinition);

        var fetchedPolicyDefinition = policyDefinitions.get(created.getContent());

        assertThat(fetchedPolicyDefinition.getContent().id()).isEqualTo(created.getContent());
        assertThat(fetchedPolicyDefinition.getContent().policy()).isNotNull();
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

        var policyDefinition = JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policyWithPermission())
                .build();

        var created = policyDefinitions.create(policyDefinition);

        var policyDefinitionList = policyDefinitions.request(policyDefinitionsQuery());

        assertThat(policyDefinitionList.isSucceeded()).isTrue();
        assertThat(policyDefinitionList.getContent())
                .anyMatch(policy -> policy.id().equals(created.getContent()));
    }

    @Test
    void should_update_a_policy_definition() {
        var id = "policyId-" + UUID.randomUUID();
        var policyDefinition = JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policyWithPermission())
                .build();

        var created = policyDefinitions.create(policyDefinition);
        assertThat(created.isSucceeded()).isTrue();

        var updateRequest = JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policyWithObligation())
                .build();
        var updated = policyDefinitions.update(updateRequest);

        assertThat(updated.isSucceeded()).isTrue();

        var fetched = policyDefinitions.get(id);
        assertThat(fetched.isSucceeded()).isTrue();
        assertThat(fetched.getContent()).isNotNull();

        var policy = fetched.getContent().policy();

        var permissions = policy.permissions();
        assertThat(permissions).isNullOrEmpty();

        var obligations = policy.obligations();

        assertThat(obligations).isNotNull().hasSize(1);
    }

    @Test
    void should_fail_to_update_a_non_existent_policy_definition() {
        var nonExistentId = "non-existent-" + UUID.randomUUID();

        var updateRequest = JsonLdPolicyDefinition.Builder.newInstance()
                .id(nonExistentId)
                .policy(policyWithObligation())
                .build();

        var updated = policyDefinitions.update(updateRequest);

        assertThat(updated.isSucceeded()).isFalse();
        assertThat(updated.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
        });
    }

    @Test
    void should_delete_a_policy_definition() {
        var id = "policyId-" + UUID.randomUUID();

        var policyDefinition = JsonLdPolicyDefinition.Builder.newInstance()
                .id(id)
                .policy(policyWithPermission())
                .build();

        var created = policyDefinitions.create(policyDefinition);

        var deleted = policyDefinitions.delete(created.getContent());
        var fetchedPolicyDefinition = policyDefinitions.get(created.getContent());

        assertThat(deleted.isSucceeded()).isTrue();
        assertThat(fetchedPolicyDefinition.isSucceeded()).isFalse();
        assertThat(fetchedPolicyDefinition.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
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

    private QuerySpec policyDefinitionsQuery() {
        return JsonLdQuerySpec.Builder.newInstance()
                .limit(10)
                .sortOrder("DESC")
                .filterExpression(emptyList())
                .build();
    }

    private JsonLdPolicy policyWithPermission() {
        var permissions = Json.createArrayBuilder()
                .add(createObjectBuilder()
                        .add(
                                ODRL_NAMESPACE + "action",
                                Json.createObjectBuilder().add("@id", ODRL_NAMESPACE + "use"))
                        .add(
                                ODRL_NAMESPACE + "constraint",
                                Json.createArrayBuilder().build()))
                .build();

        return JsonLdPolicy.Builder.newInstance()
                .raw(createObjectBuilder()
                        .add(ODRL_NAMESPACE + "permission", permissions)
                        .build())
                .build();
    }

    private JsonLdPolicy policyWithObligation() {
        var obligations = Json.createArrayBuilder()
                .add(createObjectBuilder()
                        .add(
                                ODRL_NAMESPACE + "action",
                                Json.createObjectBuilder().add("@id", ODRL_NAMESPACE + "use"))
                        .add(
                                ODRL_NAMESPACE + "constraint",
                                Json.createArrayBuilder().build()))
                .build();

        return JsonLdPolicy.Builder.newInstance()
                .raw(createObjectBuilder()
                        .add(ODRL_NAMESPACE + "obligation", obligations)
                        .build())
                .build();
    }
}
