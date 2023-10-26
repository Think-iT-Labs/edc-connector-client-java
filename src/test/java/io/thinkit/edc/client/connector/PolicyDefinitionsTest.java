package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createObjectBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.json.Json;
import java.net.http.HttpClient;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PolicyDefinitionsTest {
    @Container
    private final ManagementApiContainer prism = new ManagementApiContainer();

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

    @Test
    void should_get_a_policy_definition() {
        Result<PolicyDefinition> policyDefinition = policyDefinitions.get("definition-id");

        assertThat(policyDefinition.isSucceeded()).isTrue();
        assertThat(policyDefinition.getContent().id()).isNotBlank();
        assertThat(policyDefinition.getContent().policy()).isNotNull().satisfies(policy -> assertThat(
                        policy.getList(ODRL_NAMESPACE + "permission").size())
                .isGreaterThan(0));
        assertThat(policyDefinition.getContent().createdAt()).isGreaterThan(0);
    }

    @Test
    void should_not_get_a_policy_definition_when_id_is_empty() {
        Result<PolicyDefinition> policyDefinition = policyDefinitions.get("");

        assertThat(policyDefinition.isSucceeded()).isFalse();
        assertThat(policyDefinition.getError()).isNotNull();
    }

    @Test
    void should_create_a_policy_definition() {

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

        var policyDefinition = PolicyDefinition.Builder.newInstance()
                .id("definition-id")
                .policy(policy)
                .build();

        var created = policyDefinitions.create(policyDefinition);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should_not_create_a_policy_definition() {

        var policyDefinition =
                PolicyDefinition.Builder.newInstance().id("definition-id").build();

        var created = policyDefinitions.create(policyDefinition);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getError()).isNotNull();
    }
}
