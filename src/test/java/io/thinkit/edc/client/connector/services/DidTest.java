package io.thinkit.edc.client.connector.services;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.IdentityApiTestBase;
import io.thinkit.edc.client.connector.model.*;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DidTest extends IdentityApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private Did did;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .identityUrl(prism.getUrl())
                .build();
        did = client.did();
    }

    @Nested
    class Sync {

        @Test
        void should_get_dids() {
            var result = did.get(0, 50);
            assertThat(result).satisfies(DidTest.this::shouldGetDidsResponse);
        }

        @Test
        void should_publish_a_did() {
            var input = new DidRequestPayload("string");
            var result = did.publish(input, "id");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_unpublish_a_did() {
            var input = new DidRequestPayload("string");
            var result = did.unpublish(input, "id");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_get_did_state() {
            var input = new DidRequestPayload("string");
            var result = did.getState(input, "id");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_add_a_service_endpoint() {
            var input = new DidService("string", "string", "string");
            var result = did.addServiceEndpoint(input, "id", "did", false);
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_query_did() {
            var input = new QuerySpecInput(5, 10, "DESC", "fieldName", new CriterionInput[] {});
            var result = did.query(input, "id");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_query_did() {
            var input = new QuerySpecInput(0, 0, "wrong", "", new CriterionInput[] {});
            var result = did.query(input, "id");
            assertThat(result).satisfies(DidTest.this::errorResponse);
        }

        @Test
        void should_delete() {

            var result = did.delete("participantId", "did", "serviceId", true);
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_delete_a_verifiable_credential() {

            var result = did.delete("", "", "", null);
            assertThat(result).satisfies(DidTest.this::errorResponse);
        }

        @Test
        void should_update() {
            var input = new DidService("string", "string", "string");
            var result = did.update(input, "id", "did", false);
            assertThat(result.isSucceeded()).isTrue();
        }
    }

    @Nested
    class Async {

        @Test
        void should_get_dids_async() {
            var result = did.getAsync(0, 50);
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(DidTest.this::shouldGetDidsResponse);
        }

        @Test
        void should_get_did_state_async() {
            var input = new DidRequestPayload("string");
            var created = did.getStateAsync(input, "id");
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_publish_a_did_async() {
            var input = new DidRequestPayload("string");
            var created = did.unpublishAsync(input, "id");
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_unpublish_a_did_async() {
            var input = new DidRequestPayload("string");
            var created = did.publishAsync(input, "id");
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_add_a_service_endpoint_async() {
            var input = new DidService("string", "string", "string");
            var created = did.addServiceEndpointAsync(input, "id", "did", false);
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_query_dids_async() {
            var input = new QuerySpecInput(5, 10, "DESC", "fieldName", new CriterionInput[] {});
            var created = did.queryAsync(input, "id");
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_query_dids_async() {
            var input = new QuerySpecInput(0, 0, "wrong", "", new CriterionInput[] {});
            var result = did.queryAsync(input, "id");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(DidTest.this::errorResponse);
        }

        @Test
        void should_delete_async() {

            var deleted = did.deleteAsync("participantId", "did", "serviceID", true);
            assertThat(deleted)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_delete_async() {
            var result = did.deleteAsync("", "", "", null);
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(DidTest.this::errorResponse);
        }

        @Test
        void should_update_async() {
            var input = new DidService("string", "string", "string");
            var created = did.updateAsync(input, "id", "did", false);
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }
    }

    private <T> void errorResponse(Result<T> error) {
        assertThat(error.isSucceeded()).isFalse();
        assertThat(error.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("string");
            assertThat(apiErrorDetail.type()).isEqualTo("string");
            assertThat(apiErrorDetail.path()).isEqualTo("string");
        });
    }

    private void shouldGetDidsResponse(Result<List<DidDocument>> dids) {
        assertThat(dids.isSucceeded()).isTrue();
        assertThat(dids.getContent()).isNotNull().first().satisfies(did -> {
            assertThat(did.id()).isEqualTo("string");
            assertThat(did.service()).isNotNull().first().satisfies(service -> {
                assertThat(service.id()).isEqualTo("string");
                assertThat(service.serviceEndpoint()).isEqualTo("string");
                assertThat(service.type()).isEqualTo("string");
            });
            assertThat(did.authentication()).isNotNull().first().satisfies(service -> assertThat(service).isEqualTo("string"));
            assertThat(did.verificationMethod()).isNotNull().first().satisfies(verificationMethod -> {
                assertThat(verificationMethod.controller()).isEqualTo("string");
                assertThat(verificationMethod.controller()).isEqualTo("string");
                assertThat(verificationMethod.publicKeyMultibase()).isEqualTo("string");
                assertThat(verificationMethod.type()).isEqualTo("string");
            });
        });
    }
}
