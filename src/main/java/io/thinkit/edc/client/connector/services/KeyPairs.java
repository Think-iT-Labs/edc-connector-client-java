package io.thinkit.edc.client.connector.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.KeyDescriptor;
import io.thinkit.edc.client.connector.model.KeyPairResource;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.identity.IdentityResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KeyPairs extends IdentityResource {
    private final String url;

    public KeyPairs(EdcClientContext context) {
        super(context);
        url = "%s/v1alpha".formatted(identityUrl);
    }

    public Result<List<KeyPairResource>> getAll(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return context.httpClient().send(requestBuilder).map(this::getKeyPairs);
    }

    public CompletableFuture<Result<List<KeyPairResource>>> getAllAsync(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(this::getKeyPairs));
    }

    public Result<List<KeyPairResource>> get(String participantContextId) {
        var requestBuilder = getRequestBuilder(participantContextId);

        return context.httpClient().send(requestBuilder).map(this::getKeyPairs);
    }

    public CompletableFuture<Result<List<KeyPairResource>>> getAsync(String participantContextId) {
        var requestBuilder = getRequestBuilder(participantContextId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(this::getKeyPairs));
    }

    public Result<String> add(KeyDescriptor input, String participantContextId, Boolean makeDefault) {
        var requestBuilder = addRequestBuilder(input, participantContextId, makeDefault);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> addAsync(
            KeyDescriptor input, String participantContextId, Boolean makeDefault) {
        var requestBuilder = addRequestBuilder(input, participantContextId, makeDefault);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    public Result<KeyPairResource> getOne(String participantContextId, String keyPairId) {
        var requestBuilder = getOneRequestBuilder(participantContextId, keyPairId);

        return context.httpClient().send(requestBuilder).map(this::getKeyPairResource);
    }

    public CompletableFuture<Result<KeyPairResource>> getOneAsync(String participantContextId, String keyPairId) {
        var requestBuilder = getOneRequestBuilder(participantContextId, keyPairId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(this::getKeyPairResource));
    }

    public Result<String> activate(String participantContextId, String keyPairId) {
        var requestBuilder = activateRequestBuilder(participantContextId, keyPairId);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> activateAsync(String participantContextId, String keyPairId) {
        var requestBuilder = activateRequestBuilder(participantContextId, keyPairId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    public Result<String> revoke(String participantContextId, String keyPairId, KeyDescriptor input) {
        var requestBuilder = revokeRequestBuilder(participantContextId, keyPairId, input);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> revokeAsync(
            String participantContextId, String keyPairId, KeyDescriptor input) {
        var requestBuilder = revokeRequestBuilder(participantContextId, keyPairId, input);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    public Result<String> rotate(String participantContextId, String keyPairId, KeyDescriptor input, int duration) {
        var requestBuilder = rotateRequestBuilder(participantContextId, keyPairId, input, duration);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> rotateAsync(
            String participantContextId, String keyPairId, KeyDescriptor input, int duration) {
        var requestBuilder = rotateRequestBuilder(participantContextId, keyPairId, input, duration);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    private HttpRequest.Builder getAllRequestBuilder(int offset, int limit) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/keypairs?offset=%s&limit=%s".formatted(this.url, offset, limit)))
                .GET();
    }

    private HttpRequest.Builder getRequestBuilder(String participantContextId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/keypairs".formatted(this.url, participantContextId)))
                .GET();
    }

    private HttpRequest.Builder getOneRequestBuilder(String participantContextId, String keyPairId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/keypairs/%s".formatted(this.url, participantContextId, keyPairId)))
                .GET();
    }

    private HttpRequest.Builder addRequestBuilder(
            KeyDescriptor input, String participantContextId, Boolean makeDefault) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/keypairs?makeDefault=%s"
                        .formatted(this.url, participantContextId, makeDefault)))
                .header("content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder activateRequestBuilder(String participantId, String keyPairId) {

        return HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/participants/%s/keypairs/%s/activate".formatted(this.url, participantId, keyPairId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());
    }

    private HttpRequest.Builder revokeRequestBuilder(String participantId, String keyPairId, KeyDescriptor input) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/keypairs/%s/revoke".formatted(this.url, participantId, keyPairId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder rotateRequestBuilder(
            String participantId, String keyPairId, KeyDescriptor input, int duration) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/keypairs/%s/rotate?duration=%s"
                        .formatted(this.url, participantId, keyPairId, duration)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private List<KeyPairResource> getKeyPairs(InputStream body) {
        try {
            return context.objectMapper().readValue(body, new TypeReference<List<KeyPairResource>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyPairResource getKeyPairResource(InputStream body) {
        try {
            return context.objectMapper().readValue(body, KeyPairResource.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
