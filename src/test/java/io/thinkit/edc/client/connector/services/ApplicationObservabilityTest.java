package io.thinkit.edc.client.connector.services;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ObservabilityApiTestBase;
import io.thinkit.edc.client.connector.model.HealthStatus;
import io.thinkit.edc.client.connector.model.Result;
import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ApplicationObservabilityTest extends ObservabilityApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private ApplicationObservability applicationObservability;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .observabilityUrl(prism.getUrl())
                .build();
        applicationObservability = client.applicationObservability();
    }

    @Nested
    class Sync {
        @Test
        void should_check_health() {
            var result = applicationObservability.checkHealth();
            assertThat(result).satisfies(ApplicationObservabilityTest.this::healthStatusResponse);
        }

        @Test
        void should_check_readiness() {
            var result = applicationObservability.checkReadiness();
            assertThat(result).satisfies(ApplicationObservabilityTest.this::healthStatusResponse);
        }

        @Test
        void should_check_startup() {
            var result = applicationObservability.checkStartup();
            assertThat(result).satisfies(ApplicationObservabilityTest.this::healthStatusResponse);
        }

        @Test
        void should_check_liveness() {
            var result = applicationObservability.checkLiveness();
            assertThat(result).satisfies(ApplicationObservabilityTest.this::healthStatusResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_check_health_async() {
            var result = applicationObservability.checkHealthAsync();
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ApplicationObservabilityTest.this::healthStatusResponse);
        }

        @Test
        void should_check_readiness_async() {
            var result = applicationObservability.checkReadinessAsync();
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ApplicationObservabilityTest.this::healthStatusResponse);
        }

        @Test
        void should_check_startup_async() {
            var result = applicationObservability.checkStartupAsync();
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ApplicationObservabilityTest.this::healthStatusResponse);
        }

        @Test
        void should_check_liveness_async() {
            var result = applicationObservability.checkLivenessAsync();
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ApplicationObservabilityTest.this::healthStatusResponse);
        }
    }

    private void healthStatusResponse(Result<HealthStatus> result) {
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
