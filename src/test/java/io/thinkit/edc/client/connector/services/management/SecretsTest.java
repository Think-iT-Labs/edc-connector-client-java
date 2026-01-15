package io.thinkit.edc.client.connector.services.management;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.Secret;
import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SecretsTest extends ManagementApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private Secrets secrets;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        secrets = client.secrets();
    }

    @Nested
    class Sync {
        @Test
        void should_get_a_secret() {
            var secret = secrets.get("secret-id");
            assertThat(secret).satisfies(SecretsTest.this::shouldGetASecretResponse);
        }

        @Test
        void should_not_get_a_secret_when_id_is_empty() {
            var secret = secrets.get("");
            assertThat(secret).satisfies(SecretsTest.this::errorResponse);
        }

        @Test
        void should_create_a_secret() {

            var created = secrets.create(shouldCreateASecretRequest());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_not_create_a_secret_when_value_is_missing() {

            var created = secrets.create(shouldNotCreateASecretRequest());
            assertThat(created).satisfies(SecretsTest.this::errorResponse);
        }

        @Test
        void should_update_a_secret() {
            var created = secrets.update(shouldCreateASecretRequest());

            assertThat(created.isSucceeded()).isTrue();
        }

        @Test
        void should_not_update_a_secret_when_id_is_empty() {

            var created = secrets.update(shouldNotCreateASecretRequest());
            assertThat(created).satisfies(SecretsTest.this::errorResponse);
        }

        @Test
        void should_delete_a_secret() {
            var deleted = secrets.delete("secret-id");

            assertThat(deleted.isSucceeded()).isTrue();
        }

        @Test
        void should_not_delete_a_secret_when_id_is_empty() {
            var deleted = secrets.delete("");
            assertThat(deleted).satisfies(SecretsTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_secret_async() {
            var result = secrets.getAsync("secret-id");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(SecretsTest.this::shouldGetASecretResponse);
        }

        @Test
        void should_not_get_a_secret_when_id_is_empty_async() {
            var result = secrets.getAsync("");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(SecretsTest.this::errorResponse);
        }

        @Test
        void should_create_a_secret_async() {

            var result = secrets.createAsync(shouldCreateASecretRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_create_a_secret_when_value_is_missing_async() {
            var result = secrets.createAsync(shouldNotCreateASecretRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(SecretsTest.this::errorResponse);
        }

        @Test
        void should_update_a_secret_async() {

            var result = secrets.updateAsync(shouldCreateASecretRequest());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_not_update_a_secret_when_id_is_empty_async() {

            var result = secrets.updateAsync(shouldNotCreateASecretRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(SecretsTest.this::errorResponse);
        }

        @Test
        void should_delete_a_secret_async() {

            var result = secrets.deleteAsync("secret-id");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_not_delete_a_secret_when_id_is_empty_async() {
            var result = secrets.deleteAsync("");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(SecretsTest.this::errorResponse);
        }
    }

    private <T> void errorResponse(Result<T> error) {
        assertThat(error.isSucceeded()).isFalse();
        assertThat(error.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    private void shouldGetASecretResponse(Result<Secret> secret) {
        assertThat(secret.isSucceeded()).isTrue();
        assertThat(secret.getContent().value()).isNotBlank();
    }

    private Secret shouldCreateASecretRequest() {

        return Secret.Builder.newInstance().id("secretId").value("secretValue").build();
    }

    private Secret shouldNotCreateASecretRequest() {
        return Secret.Builder.newInstance().id("").build();
    }
}
