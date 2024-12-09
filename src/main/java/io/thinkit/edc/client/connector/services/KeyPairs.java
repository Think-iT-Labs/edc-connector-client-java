package io.thinkit.edc.client.connector.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.thinkit.edc.client.connector.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class KeyPairs {
    private final String url;
    private final EdcApiHttpClient edcApiHttpClient;

    private final ObjectMapper objectMapper;

    public KeyPairs(
            String url,
            HttpClient httpClient,
            UnaryOperator<HttpRequest.Builder> interceptor,
            ObjectMapper objectMapper) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.objectMapper = objectMapper;
        this.url = "%s/v1alpha".formatted(url);
    }

    public Result<List<KeyPairResource>> getAll(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return this.edcApiHttpClient.send(requestBuilder).map(this::getKeyPairs);
    }

    public CompletableFuture<Result<List<KeyPairResource>>> getAllAsync(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(this::getKeyPairs));
    }

    public Result<List<KeyPairResource>> get(String participantId) {
        var requestBuilder = getRequestBuilder(participantId);

        return this.edcApiHttpClient.send(requestBuilder).map(this::getKeyPairs);
    }

    public CompletableFuture<Result<List<KeyPairResource>>> getAsync(String participantId) {
        var requestBuilder = getRequestBuilder(participantId);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(this::getKeyPairs));
    }

    public Result<String> add(KeyDescriptor input, String participantId, Boolean makeDefault) {
        var requestBuilder = addRequestBuilder(input, participantId, makeDefault);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> addAsync(KeyDescriptor input, String participantId, Boolean makeDefault) {
        var requestBuilder = addRequestBuilder(input, participantId, makeDefault);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    public Result<KeyPairResource> getOne(String participantId, String keyPairId) {
        var requestBuilder = getOneRequestBuilder(participantId, keyPairId);

        return this.edcApiHttpClient.send(requestBuilder).map(this::getKeyPairResource);
    }

    public CompletableFuture<Result<KeyPairResource>> getOneAsync(String participantId, String keyPairId) {
        var requestBuilder = getOneRequestBuilder(participantId, keyPairId);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(this::getKeyPairResource));
    }

    public Result<String> activate(String participantId, String keyPairId) {
        var requestBuilder = activateRequestBuilder(participantId, keyPairId);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> activateAsync(String participantId, String keyPairId) {
        var requestBuilder = activateRequestBuilder(participantId, keyPairId);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    public Result<String> revoke(String participantId, String keyPairId, KeyDescriptor input) {
        var requestBuilder = revokeRequestBuilder(participantId, keyPairId, input);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> revokeAsync(String participantId, String keyPairId, KeyDescriptor input) {
        var requestBuilder = revokeRequestBuilder(participantId, keyPairId, input);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    public Result<String> rotate(String participantId, String keyPairId, KeyDescriptor input, int duration) {
        var requestBuilder = rotateRequestBuilder(participantId, keyPairId, input, duration);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> rotateAsync(
            String participantId, String keyPairId, KeyDescriptor input, int duration) {
        var requestBuilder = rotateRequestBuilder(participantId, keyPairId, input, duration);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    private HttpRequest.Builder getAllRequestBuilder(int offset, int limit) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/keypairs?offset=%s&limit=%s".formatted(this.url, offset, limit)))
                .GET();
    }

    private HttpRequest.Builder getRequestBuilder(String participantId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/keypairs".formatted(this.url, participantId)))
                .GET();
    }

    private HttpRequest.Builder getOneRequestBuilder(String participantId, String keyPairId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/keypairs/%s".formatted(this.url, participantId, keyPairId)))
                .GET();
    }

    private HttpRequest.Builder addRequestBuilder(KeyDescriptor input, String participantId, Boolean makeDefault) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/participants/%s/keypairs?makeDefault=%s".formatted(this.url, participantId, makeDefault)))
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
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
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
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
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
            return objectMapper.readValue(body, new TypeReference<List<KeyPairResource>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyPairResource getKeyPairResource(InputStream body) {
        try {
            return objectMapper.readValue(body, KeyPairResource.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
