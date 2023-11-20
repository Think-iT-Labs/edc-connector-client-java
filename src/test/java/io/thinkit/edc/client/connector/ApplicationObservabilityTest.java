package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class ApplicationObservabilityTest {

    @Container
    private final ManagementApiContainer prism = new ManagementApiContainer();

    private final HttpClient http = HttpClient.newBuilder().build();
    private ApplicationObservability applicationObservability;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        applicationObservability = client.applicationObservability();
    }

    @Test
    void should_check_health() {
        var result = applicationObservability.checkHealth();

        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent().isSystemHealthy()).isTrue();
        assertThat(result.getContent().componentResults()).isNotNull().first().satisfies(componentResult -> {
            assertThat(componentResult.component()).isNotNull().isEqualTo("string");
            assertThat(componentResult.isHealthy()).isTrue();
            assertThat(componentResult.failure()).isNotNull().satisfies(failure -> {
                assertThat(failure.failureDetail()).isNotNull().isEqualTo("string");
                assertThat(failure.messages().size()).isGreaterThan(0);
            });
        });
    }

    @Test
    void should_check_readiness() {
        var result = applicationObservability.checkReadiness();

        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent().isSystemHealthy()).isTrue();
        assertThat(result.getContent().componentResults()).isNotNull().first().satisfies(componentResult -> {
            assertThat(componentResult.component()).isNotNull().isEqualTo("string");
            assertThat(componentResult.isHealthy()).isTrue();
            assertThat(componentResult.failure()).isNotNull().satisfies(failure -> {
                assertThat(failure.failureDetail()).isNotNull().isEqualTo("string");
                assertThat(failure.messages().size()).isGreaterThan(0);
            });
        });
    }
}
