package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.ODRL_NAMESPACE;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
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
}
