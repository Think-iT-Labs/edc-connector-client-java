package io.thinkit.edc.client.connector.services;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.IdentityApiTestBase;
import io.thinkit.edc.client.connector.model.*;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class KeyPairsTest extends IdentityApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private KeyPairs keypairs;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .identityUrl(prism.getUrl())
                .build();
        keypairs = client.keyPairs();
    }

    @Nested
    class Sync {
        @Test
        void should_get_all_keyPairs() {
            var result = keypairs.getAll(0, 50);
            assertThat(result).satisfies(KeyPairsTest.this::shouldGetKeyPairsResponse);
        }

        @Test
        void should_get_keyPairs() {
            var result = keypairs.get("participantId");
            assertThat(result).satisfies(KeyPairsTest.this::shouldGetKeyPairsResponse);
        }

        @Test
        void should_not_get_keyPairs() {
            var result = keypairs.get("");
            assertThat(result).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_get_a_keyPair() {
            var result = keypairs.getOne("participantId", "keyPairId");
            assertThat(result).satisfies(KeyPairsTest.this::shouldGetAKeyPairResponse);
        }

        @Test
        void should_not_get_a_keyPair() {
            var result = keypairs.getOne("", "");
            assertThat(result).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_add_a_keyPair() {

            var result = keypairs.add(shouldCreateAKeyDescriptor(), "id", false);
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_add_a_keyPair() {

            var result = keypairs.add(null, "", false);
            assertThat(result).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_activate_a_keyPair() {

            var result = keypairs.activate("participantId", "keyPairId");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_activate_a_keyPair() {

            var result = keypairs.activate("", "");
            assertThat(result).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_revoke_a_keyPair() {

            var result = keypairs.revoke("participantId", "keyPairId", shouldCreateAKeyDescriptor());
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_revoke_a_keyPair() {

            var result = keypairs.revoke("", "", null);
            assertThat(result).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_rotate_a_keyPair() {

            var result = keypairs.rotate("participantId", "keyPairId", shouldCreateAKeyDescriptor(), 0);
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_rotate_a_keyPair() {

            var result = keypairs.rotate("", "", null, 0);
            assertThat(result).satisfies(KeyPairsTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_all_keyPairs_async() {
            var result = keypairs.getAllAsync(0, 50);
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(KeyPairsTest.this::shouldGetKeyPairsResponse);
        }

        @Test
        void should_get_keyPairs_async() {
            var result = keypairs.getAsync("participantId");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(KeyPairsTest.this::shouldGetKeyPairsResponse);
        }

        @Test
        void should_not_get_keyPairs_async() {
            var result = keypairs.getAsync("");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_get_a_keyPair_async() {
            var result = keypairs.getOneAsync("participantId", "keyPairId");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(KeyPairsTest.this::shouldGetAKeyPairResponse);
        }

        @Test
        void should_not_get_a_keyPair_async() {
            var result = keypairs.getOneAsync("", "");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_add_a_keyPair_async() {

            var created = keypairs.addAsync(shouldCreateAKeyDescriptor(), "id", false);
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_add_a_keyPair_async() {
            var result = keypairs.addAsync(null, "", false);
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_activate_a_keyPair_async() {

            var created = keypairs.activateAsync("participantId", "keyPairId");
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_activate_a_keyPair_async() {
            var result = keypairs.activateAsync("", "");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_revoke_a_keyPair_async() {

            var created = keypairs.revokeAsync("participantId", "keyPairId", shouldCreateAKeyDescriptor());
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_revoke_a_keyPair_async() {
            var result = keypairs.revokeAsync("", "", null);
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(KeyPairsTest.this::errorResponse);
        }

        @Test
        void should_rotate_a_keyPair_async() {

            var created = keypairs.rotateAsync("participantId", "keyPairId", shouldCreateAKeyDescriptor(), 0);
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_rotate_a_keyPair_async() {
            var result = keypairs.rotateAsync("", "", null, 0);
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(KeyPairsTest.this::errorResponse);
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

    private void shouldGetAKeyPairResponse(Result<KeyPairResource> keyPair) {
        assertThat(keyPair.getContent()).isNotNull();
        assertThat(keyPair.getContent().defaultPair()).isTrue();
        assertThat(keyPair.getContent().groupName()).isEqualTo("string");
        assertThat(keyPair.getContent().id()).isEqualTo("string");
        assertThat(keyPair.getContent().keyContext()).isEqualTo("string");
        assertThat(keyPair.getContent().keyId()).isEqualTo("string");
        assertThat(keyPair.getContent().participantId()).isEqualTo("string");
        assertThat(keyPair.getContent().privateKeyAlias()).isEqualTo("string");
        assertThat(keyPair.getContent().serializedPublicKey()).isEqualTo("string");
        assertThat(keyPair.getContent().rotationDuration()).isNotNull();
        assertThat(keyPair.getContent().state()).isEqualTo(-2147483648);
        assertThat(keyPair.getContent().timestamp()).isNotNull();
        assertThat(keyPair.getContent().useDuration()).isNotNull();
    }

    private void shouldGetKeyPairsResponse(Result<List<KeyPairResource>> keyPairs) {
        assertThat(keyPairs.isSucceeded()).isTrue();
        assertThat(keyPairs.getContent()).isNotNull().first().satisfies(keyPair -> {
            assertThat(keyPair.defaultPair()).isTrue();
            assertThat(keyPair.groupName()).isEqualTo("string");
            assertThat(keyPair.id()).isEqualTo("string");
            assertThat(keyPair.keyContext()).isEqualTo("string");
            assertThat(keyPair.keyId()).isEqualTo("string");
            assertThat(keyPair.participantId()).isEqualTo("string");
            assertThat(keyPair.privateKeyAlias()).isEqualTo("string");
            assertThat(keyPair.serializedPublicKey()).isEqualTo("string");
            assertThat(keyPair.rotationDuration()).isNotNull();
            assertThat(keyPair.state()).isEqualTo(-2147483648);
            assertThat(keyPair.timestamp()).isNotNull();
            assertThat(keyPair.useDuration()).isNotNull();
        });
    }

    private KeyDescriptor shouldCreateAKeyDescriptor() {
        Map<String, Object> key =
                Map.of("additionalProp1", Map.of(), "additionalProp2", Map.of(), "additionalProp3", Map.of());
        return new KeyDescriptor(true, key, "string", "string", key, "string", "string", "string");
    }
}
