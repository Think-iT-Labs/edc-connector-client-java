package io.thinkit.edc.client.connector.services;

import io.thinkit.edc.client.connector.model.*;

/*class PresentationsTest extends PresentationApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private Presentations presentations;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .presentation(prism.getUrl())
                .build();
        presentations = client.presentations();
    }

    @Nested
    class Sync {

        @Test
        void should_add_a_presentation() {

            var result = presentations.create(shouldCreateAPresentationRequest(), "id", "auth");
            assertThat(result).satisfies(PresentationsTest.this::shouldGetAPresentationResponse);
        }
    }

    @Nested
    class Async {

        @Test
        void should_add_a_presentation_async() {

            var created = presentations.createAsync(shouldCreateAPresentationRequest(), "id", "auth");
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(PresentationsTest.this::shouldGetAPresentationResponse);
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

    private void shouldGetAPresentationResponse(Result<PresentationResponseMessage> presentationResponseMessage) {
        assertThat(presentationResponseMessage.getContent()).isNotNull();
        assertThat(presentationResponseMessage.getContent().presentation()).isNotNull();
    }

    private PresentationQueryMessage shouldCreateAPresentationRequest() {
        List<String> scopeList = List.of(
                "org.eclipse.edc.vc.type:SomeCredential_0.3.5:write",
                "org.eclipse.edc.vc.type:SomeOtherCredential:read",
                "org.eclipse.edc.vc.type:ThirdCredential:*");
        return PresentationQueryMessage.Builder.newInstance().scope(scopeList).build();
    }
}*/
