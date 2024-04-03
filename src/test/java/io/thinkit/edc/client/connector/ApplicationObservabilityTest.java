package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.services.ApplicationObservability;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled // will be tackled in https://github.com/Think-iT-Labs/edc-connector-client-java/issues/125
class ApplicationObservabilityTest extends ContainerTestBase {

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
    void should_check_health_async() {
        try {
            var result = applicationObservability.checkHealthAsync().get();
            assertThat(result.isSucceeded()).isTrue();
            assertThat(result.getContent().isSystemHealthy()).isTrue();
            assertThat(result.getContent().componentResults())
                    .isNotNull()
                    .first()
                    .satisfies(componentResult -> {
                        assertThat(componentResult.component()).isNotNull().isEqualTo("string");
                        assertThat(componentResult.isHealthy()).isTrue();
                        assertThat(componentResult.failure()).isNotNull().satisfies(failure -> {
                            assertThat(failure.failureDetail()).isNotNull().isEqualTo("string");
                            assertThat(failure.messages().size()).isGreaterThan(0);
                        });
                    });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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

    @Test
    void should_check_readiness_async() {
        try {
            var result = applicationObservability.checkReadinessAsync().get();
            assertThat(result.isSucceeded()).isTrue();
            assertThat(result.getContent().isSystemHealthy()).isTrue();
            assertThat(result.getContent().componentResults())
                    .isNotNull()
                    .first()
                    .satisfies(componentResult -> {
                        assertThat(componentResult.component()).isNotNull().isEqualTo("string");
                        assertThat(componentResult.isHealthy()).isTrue();
                        assertThat(componentResult.failure()).isNotNull().satisfies(failure -> {
                            assertThat(failure.failureDetail()).isNotNull().isEqualTo("string");
                            assertThat(failure.messages().size()).isGreaterThan(0);
                        });
                    });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_check_startup() {
        var result = applicationObservability.checkStartup();

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
    void should_check_startup_async() {
        try {
            var result = applicationObservability.checkStartupAsync().get();
            assertThat(result.isSucceeded()).isTrue();
            assertThat(result.getContent().isSystemHealthy()).isTrue();
            assertThat(result.getContent().componentResults())
                    .isNotNull()
                    .first()
                    .satisfies(componentResult -> {
                        assertThat(componentResult.component()).isNotNull().isEqualTo("string");
                        assertThat(componentResult.isHealthy()).isTrue();
                        assertThat(componentResult.failure()).isNotNull().satisfies(failure -> {
                            assertThat(failure.failureDetail()).isNotNull().isEqualTo("string");
                            assertThat(failure.messages().size()).isGreaterThan(0);
                        });
                    });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_check_liveness() {
        var result = applicationObservability.checkLiveness();

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
    void should_check_liveness_async() {
        try {
            var result = applicationObservability.checkLivenessAsync().get();
            assertThat(result.isSucceeded()).isTrue();
            assertThat(result.getContent().isSystemHealthy()).isTrue();
            assertThat(result.getContent().componentResults())
                    .isNotNull()
                    .first()
                    .satisfies(componentResult -> {
                        assertThat(componentResult.component()).isNotNull().isEqualTo("string");
                        assertThat(componentResult.isHealthy()).isTrue();
                        assertThat(componentResult.failure()).isNotNull().satisfies(failure -> {
                            assertThat(failure.failureDetail()).isNotNull().isEqualTo("string");
                            assertThat(failure.messages().size()).isGreaterThan(0);
                        });
                    });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
