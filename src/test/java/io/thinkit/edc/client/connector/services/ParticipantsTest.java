package io.thinkit.edc.client.connector.services;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.IdentityApiTestBase;
import io.thinkit.edc.client.connector.model.*;
import java.net.http.HttpClient;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParticipantsTest extends IdentityApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private Participants participants;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .identityUrl(prism.getUrl())
                .build();
        participants = client.participants();
    }

    @Nested
    class Sync {

        @Test
        void should_get_all_participants() {
            var result = participants.getAll(0, 50);
            assertThat(result).satisfies(ParticipantsTest.this::shouldGetParticipantsResponse);
        }

        @Test
        void should_add_a_participant() {

            var result = participants.create(shouldCreateAParticipant());
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_get_a_participant() {
            var result = participants.get("participantId");
            assertThat(result).satisfies(ParticipantsTest.this::shouldGetAParticipantResponse);
        }

        @Test
        void should_not_get_a_participant() {
            var result = participants.get("");
            assertThat(result).satisfies(ParticipantsTest.this::errorResponse);
        }

        @Test
        void should_delete_a_participant() {

            var result = participants.delete("participantId");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_delete_a_participant() {

            var result = participants.delete("");
            assertThat(result).satisfies(ParticipantsTest.this::errorResponse);
        }

        @Test
        void should_update_a_participant() {

            var result = participants.update(List.of("string"), "id");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_update_a_participant() {

            var result = participants.update(null, "");
            assertThat(result).satisfies(ParticipantsTest.this::errorResponse);
        }

        @Test
        void should_activate_a_participant() {

            var result = participants.activate(shouldCreateAParticipant(), "id", true);
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_activate_a_participant() {

            var result = participants.activate(null, "", true);
            assertThat(result).satisfies(ParticipantsTest.this::errorResponse);
        }

        @Test
        void should_generate_token_for_a_participant() {

            var result = participants.generateToken(shouldCreateAParticipant(), "id");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_generate_token_for_a_participant() {

            var result = participants.generateToken(null, "");
            assertThat(result).satisfies(ParticipantsTest.this::errorResponse);
        }
    }

    @Nested
    class Async {

        @Test
        void should_get_all_participants_async() {
            var result = participants.getAllAsync(0, 50);
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ParticipantsTest.this::shouldGetParticipantsResponse);
        }

        @Test
        void should_add_a_participant_async() {

            var created = participants.createAsync(shouldCreateAParticipant());
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_get_a_participant_async() {
            var result = participants.getAsync("participantId");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ParticipantsTest.this::shouldGetAParticipantResponse);
        }

        @Test
        void should_not_get_a_participant_async() {
            var result = participants.getAsync("");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ParticipantsTest.this::errorResponse);
        }

        @Test
        void should_delete_a_participant_async() {

            var deleted = participants.deleteAsync("participantId");
            assertThat(deleted)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_delete_a_participant_async() {
            var result = participants.deleteAsync("");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ParticipantsTest.this::errorResponse);
        }

        @Test
        void should_update_a_participant_async() {

            var updated = participants.updateAsync(List.of("string"), "id");
            assertThat(updated)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_update_a_participant_async() {
            var result = participants.updateAsync(null, "");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ParticipantsTest.this::errorResponse);
        }

        @Test
        void should_activate_a_participant_async() {

            var created = participants.activateAsync(shouldCreateAParticipant(), "id", true);
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_activate_a_participant_async() {

            var result = participants.activateAsync(null, "", true);
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ParticipantsTest.this::errorResponse);
        }

        @Test
        void should_generate_token_for_a_participant_async() {

            var created = participants.generateTokenAsync(shouldCreateAParticipant(), "id");
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(result -> assertThat(result.isSucceeded()).isTrue());
        }

        @Test
        void should_not_generate_token_for_a_participant_async() {

            var result = participants.generateTokenAsync(null, "");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ParticipantsTest.this::errorResponse);
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

    private void shouldGetAParticipantResponse(Result<ParticipantContext> participantContext) {
        assertThat(participantContext.getContent()).isNotNull();
        assertThat(participantContext.getContent().apiTokenAlias()).isEqualTo("string");
        assertThat(participantContext.getContent().createdAt()).isNotNull();
        assertThat(participantContext.getContent().did()).isEqualTo("string");
        assertThat(participantContext.getContent().lastModified()).isNotNull();
        assertThat(participantContext.getContent().participantId()).isEqualTo("string");
        assertThat(participantContext.getContent().state()).isEqualTo(-2147483648);
        assertThat(participantContext.getContent().roles()).isNotNull().first().satisfies(role -> assertThat(role)
                .isEqualTo("string"));
    }

    private void shouldGetParticipantsResponse(Result<List<ParticipantContext>> participantsContext) {
        assertThat(participantsContext.isSucceeded()).isTrue();
        assertThat(participantsContext.getContent()).isNotNull().first().satisfies(participantContext -> {
            assertThat(participantContext.apiTokenAlias()).isEqualTo("string");
            assertThat(participantContext.createdAt()).isNotNull();
            assertThat(participantContext.did()).isEqualTo("string");
            assertThat(participantContext.lastModified()).isNotNull();
            assertThat(participantContext.participantId()).isEqualTo("string");
            assertThat(participantContext.state()).isEqualTo(-2147483648);
            assertThat(participantContext.roles()).isNotNull().first().satisfies(role -> assertThat(role)
                    .isEqualTo("string"));
        });
    }

    private ParticipantManifest shouldCreateAParticipant() {
        var service = new ServiceInput("string", "string", "string");
        Map<String, Object> properties =
                Map.of("additionalProp1", Map.of(), "additionalProp2", Map.of(), "additionalProp3", Map.of());
        var keyDescriptor =
                new KeyDescriptor(true, properties, "string", "string", properties, "string", "string", "string");
        return new ParticipantManifest(
                true, properties, "string", keyDescriptor, "string", List.of("string"), List.of(service));
    }
}
