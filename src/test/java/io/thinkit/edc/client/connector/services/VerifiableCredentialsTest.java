package io.thinkit.edc.client.connector.services;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.IdentityApiTestBase;
import io.thinkit.edc.client.connector.model.*;
import java.net.http.HttpClient;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VerifiableCredentialsTest extends IdentityApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private VerifiableCredentials verifiableCredentials;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .identityUrl(prism.getUrl())
                .build();
        verifiableCredentials = client.verifiableCredentials();
    }

    @Nested
    class Sync {
        @Test
        void should_get_a_verifiable_credential() {
            var result = verifiableCredentials.get("participantId", "credentialId");
            assertThat(result).satisfies(VerifiableCredentialsTest.this::shouldGetAVerifiableCredentialResponse);
        }

        @Test
        void should_not_get_a_verifiable_credential() {
            var result = verifiableCredentials.get("", "");
            assertThat(result).satisfies(VerifiableCredentialsTest.this::errorResponse);
        }

        @Test
        void should_get_verifiable_credentials() {
            var result = verifiableCredentials.getList("participantId", "type");
            assertThat(result).satisfies(VerifiableCredentialsTest.this::shouldGetVerifiableCredentialsResponse);
        }

        @Test
        void should_not_get_verifiable_credentials() {
            var result = verifiableCredentials.getList("", "type");
            assertThat(result).satisfies(VerifiableCredentialsTest.this::errorResponse);
        }

        @Test
        void should_get_all_verifiable_credentials() {
            var result = verifiableCredentials.getAll(0, 50);
            assertThat(result).satisfies(VerifiableCredentialsTest.this::shouldGetVerifiableCredentialsResponse);
        }

        @Test
        void should_add_a_verifiable_credential() {

            var result = verifiableCredentials.create(shouldCreateAVerifiableCredentialRequest(), "id");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_add_a_verifiable_credential() {

            var result = verifiableCredentials.create(null, "");
            assertThat(result).satisfies(VerifiableCredentialsTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_verifiable_credential_async() {
            var result = verifiableCredentials.getAsync("participantId", "credentialId");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(VerifiableCredentialsTest.this::shouldGetAVerifiableCredentialResponse);
        }

        @Test
        void should_not_get_a_verifiable_credential_async() {
            var result = verifiableCredentials.getAsync("", "");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(VerifiableCredentialsTest.this::errorResponse);
        }

        @Test
        void should_get_verifiable_credentials_async() {
            var result = verifiableCredentials.getListAsync("participantId", "type");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(VerifiableCredentialsTest.this::shouldGetVerifiableCredentialsResponse);
        }

        @Test
        void should_not_get_verifiable_credentials_async() {
            var result = verifiableCredentials.getListAsync("", "type");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(VerifiableCredentialsTest.this::errorResponse);
        }

        @Test
        void should_get_all_verifiable_credentials_async() {
            var result = verifiableCredentials.getAllAsync(0, 50);
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(VerifiableCredentialsTest.this::shouldGetVerifiableCredentialsResponse);
        }

        @Test
        void should_add_a_verifiable_credential_async() {

            var created = verifiableCredentials.createAsync(shouldCreateAVerifiableCredentialRequest(), "id");
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_add_a_verifiable_credential_async() {
            var result = verifiableCredentials.createAsync(null, "");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(VerifiableCredentialsTest.this::errorResponse);
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

    private void shouldGetAVerifiableCredentialResponse(Result<VerifiableCredentialResource> verifiableCredentials) {
        assertThat(verifiableCredentials.getContent()).isNotNull();
        assertThat(verifiableCredentials.getContent().credentialStatus()).isEqualTo("INITIAL");
        assertThat(verifiableCredentials.getContent().holderId()).isEqualTo("string");
        assertThat(verifiableCredentials.getContent().issuerId()).isEqualTo("string");
        assertThat(verifiableCredentials.getContent().participantId()).isEqualTo("string");
        assertThat(verifiableCredentials.getContent().state()).isEqualTo(-2147483648);
        assertThat(verifiableCredentials.getContent().timeOfLastStatusUpdate()).isEqualTo("2019-08-24T14:15:22Z");
        assertThat(verifiableCredentials.getContent().issuancePolicy()).isNotNull();
        assertThat(verifiableCredentials.getContent().reissuancePolicy()).isNotNull();
        assertThat(verifiableCredentials.getContent().verifiableCredential())
                .isNotNull()
                .satisfies(verifiableCredential -> {
                    assertThat(verifiableCredential.format()).isEqualTo("JSON_LD");
                    assertThat(verifiableCredential.rawVc()).isEqualTo("string");
                    assertThat(verifiableCredential.credential()).isNotNull().satisfies(credential -> {
                        assertThat(credential.description()).isEqualTo("string");
                        assertThat(credential.expirationDate()).isEqualTo("2019-08-24T14:15:22Z");
                        assertThat(credential.issuanceDate()).isEqualTo("2019-08-24T14:15:22Z");
                        assertThat(credential.issuer()).isNotNull();
                        assertThat(credential.name()).isEqualTo("string");
                    });
                });
    }

    private void shouldGetVerifiableCredentialsResponse(
            Result<List<VerifiableCredentialResource>> verifiableCredentials) {
        assertThat(verifiableCredentials.isSucceeded()).isTrue();
        assertThat(verifiableCredentials.getContent()).isNotNull().first().satisfies(verifiable -> {
            assertThat(verifiable.credentialStatus()).isEqualTo("INITIAL");
            assertThat(verifiable.holderId()).isEqualTo("string");
            assertThat(verifiable.issuerId()).isEqualTo("string");
            assertThat(verifiable.participantId()).isEqualTo("string");
            assertThat(verifiable.state()).isEqualTo(-2147483648);
            assertThat(verifiable.timeOfLastStatusUpdate()).isEqualTo("2019-08-24T14:15:22Z");
            assertThat(verifiable.issuancePolicy()).isNotNull();
            assertThat(verifiable.reissuancePolicy()).isNotNull();
            assertThat(verifiable.verifiableCredential()).isNotNull().satisfies(verifiableCredential -> {
                assertThat(verifiableCredential.format()).isEqualTo("JSON_LD");
                assertThat(verifiableCredential.rawVc()).isEqualTo("string");
                assertThat(verifiableCredential.credential()).isNotNull().satisfies(credential -> {
                    assertThat(credential.description()).isEqualTo("string");
                    assertThat(credential.expirationDate()).isEqualTo("2019-08-24T14:15:22Z");
                    assertThat(credential.issuanceDate()).isEqualTo("2019-08-24T14:15:22Z");
                    assertThat(credential.issuer()).isNotNull();
                    assertThat(credential.name()).isEqualTo("string");
                });
            });
        });
    }

    private VerifiableCredentialManifest shouldCreateAVerifiableCredentialRequest() {
        Map<String, Object> additionalProperties = Map.of("additionalProperties", Map.of());
        CredentialStatus credentialStatus = new CredentialStatus("string", "string", additionalProperties);

        CredentialSubject credentialSubject = new CredentialSubject("string");
        Issuer issuer = new Issuer("string", additionalProperties);
        VerifiableCredential verifiableCredential = new VerifiableCredential(
                "string",
                Collections.singletonList(credentialStatus),
                Collections.singletonList(credentialSubject),
                "description",
                "2024-10-02T13:09:06.963Z",
                "2024-10-02T13:09:06.963Z",
                issuer,
                "name",
                List.of("string"));

        VerifiableCredentialContainer verifiableCredentialContainer =
                new VerifiableCredentialContainer(verifiableCredential, "JSON_LD", "string");
        Map<String, Object> policy = new HashMap<>();
        policy.put("@type", "SET");
        policy.put("assignee", "string");
        policy.put("assigner", "assigner");
        return new VerifiableCredentialManifest("id", policy, "participantId", policy, verifiableCredentialContainer);
    }
}
