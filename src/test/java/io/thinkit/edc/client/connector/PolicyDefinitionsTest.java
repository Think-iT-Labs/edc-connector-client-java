package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PolicyDefinitionsTest {
    @Container
    private GenericContainer<?> prism = new GenericContainer<>("stoplight/prism:3.3.4")
            .withClasspathResourceMapping("/management-api.yml", "/management-api.yml", BindMode.READ_WRITE)
            .withCommand("mock -h 0.0.0.0 -d /management-api.yml")
            .withExposedPorts(4010)
            .withLogConsumer(frame -> {
                if (!frame.getUtf8String().contains("[CLI]")) {
                    System.out.print(frame.getUtf8String());
                }
            });

    private final HttpClient http = HttpClient.newBuilder().build();
    private PolicyDefinitions policyDefinitions;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .interceptor(r -> r.header("Prefer", "dynamic=false"))
                .managementUrl("http://127.0.0.1:%s".formatted(prism.getFirstMappedPort()))
                .build();
        policyDefinitions = client.policyDefinitions();
    }

    @Test
    void should_get_a_policy_definition() {
        Result<PolicyDefinition> policyDefinition = policyDefinitions.get("definition-id");

        assertThat(policyDefinition.getContent().id()).isNotBlank();
        assertThat(policyDefinition.getContent().policy()).isNotNull();
    }
}
