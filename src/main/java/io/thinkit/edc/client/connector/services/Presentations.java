package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.PresentationQueryMessage;
import io.thinkit.edc.client.connector.model.PresentationResponseMessage;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.presentation.PresentationResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;

public class Presentations extends PresentationResource {
    private final String url;

    public Presentations(EdcClientContext context) {
        super(context);
        url = "%s/v1".formatted(presentationUrl);
    }

    public Result<PresentationResponseMessage> create(
            PresentationQueryMessage input, String participantId, String authorization) {
        var requestBuilder = createRequestBuilder(input, participantId, authorization);

        return this.context
                .httpClient()
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getResponse);
    }

    public CompletableFuture<Result<PresentationResponseMessage>> createAsync(
            PresentationQueryMessage input, String participantId, String authorization) {
        var requestBuilder = createRequestBuilder(input, participantId, authorization);

        return this.context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getResponse));
    }

    private HttpRequest.Builder createRequestBuilder(
            PresentationQueryMessage input, String participantId, String authorization) {
        var requestBody = compact(
                input,
                Json.createObjectBuilder()
                        .add(
                                "@context",
                                Json.createObjectBuilder()
                                        .add(
                                                "https://identity.foundation/presentation-exchange/submission/v1",
                                                Json.createObjectBuilder().build())
                                        .build())
                        .build());
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/presentations/query".formatted(this.url, participantId)))
                .header("content-type", "application/json")
                .header("Authorization", authorization)
                .POST(ofString(requestBody.toString()));
    }

    private PresentationResponseMessage getResponse(JsonArray array) {
        return PresentationResponseMessage.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }
}
