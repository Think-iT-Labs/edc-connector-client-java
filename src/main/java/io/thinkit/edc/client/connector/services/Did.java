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

public class Did {
    private final String url;
    private final EdcApiHttpClient edcApiHttpClient;

    private final ObjectMapper objectMapper;

    public Did(
            String url,
            HttpClient httpClient,
            UnaryOperator<HttpRequest.Builder> interceptor,
            ObjectMapper objectMapper) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.objectMapper = objectMapper;
        this.url = "%s/v1alpha".formatted(url);
    }

    public Result<List<DidDocument>> get(int offset, int limit) {
        var requestBuilder = getRequestBuilder(offset, limit);

        return this.edcApiHttpClient.send(requestBuilder).map(this::getDidDocuments);
    }

    public CompletableFuture<Result<List<DidDocument>>> getAsync(int offset, int limit) {
        var requestBuilder = getRequestBuilder(offset, limit);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(this::getDidDocuments));
    }

    public Result<String> publish(DidRequestPayload input, String participantId) {
        var requestBuilder = publishRequestBuilder(input, participantId);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> publishAsync(DidRequestPayload input, String participantId) {
        var requestBuilder = publishRequestBuilder(input, participantId);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    public Result<String> unpublish(DidRequestPayload input, String participantId) {
        var requestBuilder = unpublishRequestBuilder(input, participantId);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> unpublishAsync(DidRequestPayload input, String participantId) {
        var requestBuilder = unpublishRequestBuilder(input, participantId);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    public Result<String> getState(DidRequestPayload input, String participantId) {
        var requestBuilder = getStateRequestBuilder(input, participantId);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> getStateAsync(DidRequestPayload input, String participantId) {
        var requestBuilder = getStateRequestBuilder(input, participantId);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    public Result<String> addServiceEndpoint(DidService input, String participantId, String did, Boolean autoPublish) {
        var requestBuilder = addServiceEndpointRequestBuilder(input, participantId, did, autoPublish);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> addServiceEndpointAsync(
            DidService input, String participantId, String did, Boolean autoPublish) {
        var requestBuilder = addServiceEndpointRequestBuilder(input, participantId, did, autoPublish);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    public Result<List<DidDocument>> query(QuerySpecInput input, String participantId) {
        var requestBuilder = queryRequestBuilder(input, participantId);

        return this.edcApiHttpClient.send(requestBuilder).map(this::getDidDocuments);
    }

    public CompletableFuture<Result<List<DidDocument>>> queryAsync(QuerySpecInput input, String participantId) {
        var requestBuilder = queryRequestBuilder(input, participantId);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(this::getDidDocuments));
    }

    public Result<String> delete(String participantId, String did, String serviceId, Boolean autoPublish) {
        var requestBuilder = deleteRequestBuilder(participantId, did, serviceId, autoPublish);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> deleteAsync(
            String participantId, String did, String serviceId, Boolean autoPublish) {
        var requestBuilder = deleteRequestBuilder(participantId, did, serviceId, autoPublish);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    public Result<String> update(DidService input, String participantId, String did, Boolean autoPublish) {
        var requestBuilder = updateRequestBuilder(input, participantId, did, autoPublish);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> participantId);
    }

    public CompletableFuture<Result<String>> updateAsync(
            DidService input, String participantId, String did, Boolean autoPublish) {
        var requestBuilder = updateRequestBuilder(input, participantId, did, autoPublish);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantId));
    }

    private HttpRequest.Builder getRequestBuilder(int offset, int limit) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/dids?offset=%s&limit=%s".formatted(this.url, offset, limit)))
                .GET();
    }

    private HttpRequest.Builder queryRequestBuilder(QuerySpecInput input, String participantId) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/query".formatted(this.url, participantId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder deleteRequestBuilder(
            String participantId, String did, String serviceId, Boolean autoPublish) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/%s/endpoints?serviceId=%s&autoPublish=%s"
                        .formatted(this.url, participantId, did, serviceId, autoPublish)))
                .DELETE();
    }

    private HttpRequest.Builder updateRequestBuilder(
            DidService input, String participantId, String did, Boolean autoPublish) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/%s/endpoints?autoPublish=%s"
                        .formatted(this.url, participantId, did, autoPublish)))
                .header("content-type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder publishRequestBuilder(DidRequestPayload input, String participantId) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/publish".formatted(this.url, participantId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder unpublishRequestBuilder(DidRequestPayload input, String participantId) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/unpublish".formatted(this.url, participantId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder getStateRequestBuilder(DidRequestPayload input, String participantId) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/state".formatted(this.url, participantId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder addServiceEndpointRequestBuilder(
            DidService input, String participantId, String did, Boolean autoPublish) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/%s/endpoints?autoPublish=%s"
                        .formatted(this.url, participantId, did, autoPublish)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private List<DidDocument> getDidDocuments(InputStream body) {
        try {
            return objectMapper.readValue(body, new TypeReference<List<DidDocument>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
