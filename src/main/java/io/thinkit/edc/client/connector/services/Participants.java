package io.thinkit.edc.client.connector.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.ParticipantContext;
import io.thinkit.edc.client.connector.model.ParticipantManifest;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.identity.IdentityResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Participants extends IdentityResource {

    private final String url;

    public Participants(EdcClientContext context) {
        super(context);
        url = "%s/v1alpha/participants".formatted(identityUrl);
    }

    public Result<List<ParticipantContext>> getAll(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return context.httpClient().send(requestBuilder).map(this::getParticipants);
    }

    public CompletableFuture<Result<List<ParticipantContext>>> getAllAsync(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(this::getParticipants));
    }

    public Result<String> create(ParticipantManifest input) {
        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(result -> input.participantId());
    }

    public CompletableFuture<Result<String>> createAsync(ParticipantManifest input) {
        var requestBuilder = createRequestBuilder(input);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> input.participantId()));
    }

    public Result<ParticipantContext> get(String participantId) {
        var requestBuilder = getRequestBuilder(participantId);

        return context.httpClient().send(requestBuilder).map(this::getParticipant);
    }

    public CompletableFuture<Result<ParticipantContext>> getAsync(String participantId) {
        var requestBuilder = getRequestBuilder(participantId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(this::getParticipant));
    }

    public Result<String> delete(String participantId) {
        var requestBuilder = deleteRequestBuilder(participantId);

        return context.httpClient().send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> deleteAsync(String participantId) {
        var requestBuilder = deleteRequestBuilder(participantId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> participantId));
    }

    public Result<String> update(List<String> input, String participantId) {
        var requestBuilder = updateRequestBuilder(input, participantId);

        return context.httpClient().send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> updateAsync(List<String> input, String participantId) {
        var requestBuilder = updateRequestBuilder(input, participantId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> participantId));
    }

    public Result<String> activate(ParticipantManifest input, String participantId, Boolean isActive) {
        var requestBuilder = activateRequestBuilder(input, participantId, isActive);

        return context.httpClient().send(requestBuilder).map(result -> input.participantId());
    }

    public CompletableFuture<Result<String>> activateAsync(
            ParticipantManifest input, String participantId, Boolean isActive) {
        var requestBuilder = activateRequestBuilder(input, participantId, isActive);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> input.participantId()));
    }

    public Result<String> generateToken(ParticipantManifest input, String participantId) {
        var requestBuilder = generateTokenRequestBuilder(input, participantId);

        return context.httpClient().send(requestBuilder).map(result -> input.participantId());
    }

    public CompletableFuture<Result<String>> generateTokenAsync(ParticipantManifest input, String participantId) {
        var requestBuilder = generateTokenRequestBuilder(input, participantId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> input.participantId()));
    }

    private HttpRequest.Builder getRequestBuilder(String participantId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, participantId)))
                .GET();
    }

    private HttpRequest.Builder getAllRequestBuilder(int offset, int limit) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s?offset=%s&limit=%s".formatted(this.url, offset, limit)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(ParticipantManifest input) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder updateRequestBuilder(List<String> input, String participantId) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/roles".formatted(this.url, participantId)))
                .header("content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder deleteRequestBuilder(String participantId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, participantId)))
                .DELETE();
    }

    private HttpRequest.Builder activateRequestBuilder(
            ParticipantManifest input, String participantId, Boolean isActive) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/state?isActive=%s".formatted(this.url, participantId, isActive)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder generateTokenRequestBuilder(ParticipantManifest input, String participantId) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/token".formatted(this.url, participantId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private ParticipantContext getParticipant(InputStream body) {
        try {
            return context.objectMapper().readValue(body, ParticipantContext.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ParticipantContext> getParticipants(InputStream body) {
        try {
            return context.objectMapper().readValue(body, new TypeReference<List<ParticipantContext>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
