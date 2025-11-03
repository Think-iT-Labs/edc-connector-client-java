package io.thinkit.edc.client.connector.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.DidDocument;
import io.thinkit.edc.client.connector.model.DidRequestPayload;
import io.thinkit.edc.client.connector.model.QuerySpecInput;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.ServiceInput;
import io.thinkit.edc.client.connector.resource.identity.IdentityResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Did extends IdentityResource {

    private final String url;

    public Did(EdcClientContext context) {
        super(context);
        url = "%s/v1alpha".formatted(identityUrl);
    }

    public Result<List<DidDocument>> get(int offset, int limit) {
        var requestBuilder = getRequestBuilder(offset, limit);

        return context.httpClient().send(requestBuilder).map(this::getDidDocuments);
    }

    public CompletableFuture<Result<List<DidDocument>>> getAsync(int offset, int limit) {
        var requestBuilder = getRequestBuilder(offset, limit);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(this::getDidDocuments));
    }

    public Result<String> publish(DidRequestPayload input, String participantContextId) {
        var requestBuilder = publishRequestBuilder(input, participantContextId);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> publishAsync(DidRequestPayload input, String participantContextId) {
        var requestBuilder = publishRequestBuilder(input, participantContextId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    public Result<String> unpublish(DidRequestPayload input, String participantContextId) {
        var requestBuilder = unpublishRequestBuilder(input, participantContextId);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> unpublishAsync(DidRequestPayload input, String participantContextId) {
        var requestBuilder = unpublishRequestBuilder(input, participantContextId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    public Result<String> getState(DidRequestPayload input, String participantContextId) {
        var requestBuilder = getStateRequestBuilder(input, participantContextId);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> getStateAsync(DidRequestPayload input, String participantContextId) {
        var requestBuilder = getStateRequestBuilder(input, participantContextId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    public Result<String> addServiceEndpoint(
            ServiceInput input, String participantContextId, String did, Boolean autoPublish) {
        var requestBuilder = addServiceEndpointRequestBuilder(input, participantContextId, did, autoPublish);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> addServiceEndpointAsync(
            ServiceInput input, String participantContextId, String did, Boolean autoPublish) {
        var requestBuilder = addServiceEndpointRequestBuilder(input, participantContextId, did, autoPublish);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    public Result<List<DidDocument>> query(QuerySpecInput input, String participantContextId) {
        var requestBuilder = queryRequestBuilder(input, participantContextId);

        return context.httpClient().send(requestBuilder).map(this::getDidDocuments);
    }

    public CompletableFuture<Result<List<DidDocument>>> queryAsync(QuerySpecInput input, String participantContextId) {
        var requestBuilder = queryRequestBuilder(input, participantContextId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(this::getDidDocuments));
    }

    public Result<String> delete(String participantContextId, String did, String serviceId, Boolean autoPublish) {
        var requestBuilder = deleteRequestBuilder(participantContextId, did, serviceId, autoPublish);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> deleteAsync(
            String participantContextId, String did, String serviceId, Boolean autoPublish) {
        var requestBuilder = deleteRequestBuilder(participantContextId, did, serviceId, autoPublish);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    public Result<String> update(ServiceInput input, String participantContextId, String did, Boolean autoPublish) {
        var requestBuilder = updateRequestBuilder(input, participantContextId, did, autoPublish);

        return context.httpClient().send(requestBuilder).map(result -> participantContextId);
    }

    public CompletableFuture<Result<String>> updateAsync(
            ServiceInput input, String participantContextId, String did, Boolean autoPublish) {
        var requestBuilder = updateRequestBuilder(input, participantContextId, did, autoPublish);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    private HttpRequest.Builder getRequestBuilder(int offset, int limit) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/dids?offset=%s&limit=%s".formatted(this.url, offset, limit)))
                .GET();
    }

    private HttpRequest.Builder queryRequestBuilder(QuerySpecInput input, String participantContextId) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/query".formatted(this.url, participantContextId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder deleteRequestBuilder(
            String participantContextId, String did, String serviceId, Boolean autoPublish) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/%s/endpoints?serviceId=%s&autoPublish=%s"
                        .formatted(this.url, participantContextId, did, serviceId, autoPublish)))
                .DELETE();
    }

    private HttpRequest.Builder updateRequestBuilder(
            ServiceInput input, String participantContextId, String did, Boolean autoPublish) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/%s/endpoints?autoPublish=%s"
                        .formatted(this.url, participantContextId, did, autoPublish)))
                .header("content-type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder publishRequestBuilder(DidRequestPayload input, String participantContextId) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/publish".formatted(this.url, participantContextId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder unpublishRequestBuilder(DidRequestPayload input, String participantContextId) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/unpublish".formatted(this.url, participantContextId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder getStateRequestBuilder(DidRequestPayload input, String participantContextId) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/state".formatted(this.url, participantContextId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder addServiceEndpointRequestBuilder(
            ServiceInput input, String participantContextId, String did, Boolean autoPublish) {
        String requestBody;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/dids/%s/endpoints?autoPublish=%s"
                        .formatted(this.url, participantContextId, did, autoPublish)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private List<DidDocument> getDidDocuments(InputStream body) {
        try {
            return context.objectMapper().readValue(body, new TypeReference<List<DidDocument>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
