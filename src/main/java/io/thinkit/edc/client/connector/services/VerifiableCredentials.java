package io.thinkit.edc.client.connector.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.VerifiableCredentialManifest;
import io.thinkit.edc.client.connector.model.VerifiableCredentialResource;
import io.thinkit.edc.client.connector.resource.identity.IdentityResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VerifiableCredentials extends IdentityResource {

    private final String url;

    public VerifiableCredentials(EdcClientContext context) {
        super(context);
        url = "%s/v1alpha".formatted(identityUrl);
    }

    public Result<VerifiableCredentialResource> get(String participantContextId, String credentialId) {
        var requestBuilder = getRequestBuilder(participantContextId, credentialId);

        return context.httpClient().send(requestBuilder).map(this::getVerifiableCredential);
    }

    public CompletableFuture<Result<VerifiableCredentialResource>> getAsync(
            String participantContextId, String credentialId) {
        var requestBuilder = getRequestBuilder(participantContextId, credentialId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(this::getVerifiableCredential));
    }

    public Result<List<VerifiableCredentialResource>> getList(String participantContextId, String type) {
        var requestBuilder = getListRequestBuilder(participantContextId, type);

        return context.httpClient().send(requestBuilder).map(this::getVerifiableCredentials);
    }

    public CompletableFuture<Result<List<VerifiableCredentialResource>>> getListAsync(
            String participantContextId, String type) {
        var requestBuilder = getListRequestBuilder(participantContextId, type);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(this::getVerifiableCredentials));
    }

    public Result<List<VerifiableCredentialResource>> getAll(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return context.httpClient().send(requestBuilder).map(this::getVerifiableCredentials);
    }

    public CompletableFuture<Result<List<VerifiableCredentialResource>>> getAllAsync(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(this::getVerifiableCredentials));
    }

    public Result<String> create(VerifiableCredentialManifest input, String participantContextId) {
        var requestBuilder = createRequestBuilder(input, participantContextId);

        return context.httpClient().send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> createAsync(
            VerifiableCredentialManifest input, String participantContextId) {
        var requestBuilder = createRequestBuilder(input, participantContextId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> update(VerifiableCredentialManifest input, String participantContextId) {
        var requestBuilder = updateRequestBuilder(input, participantContextId);

        return context.httpClient().send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> updateAsync(
            VerifiableCredentialManifest input, String participantContextId) {
        var requestBuilder = updateRequestBuilder(input, participantContextId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> delete(String participantContextId, String credentialId) {
        var requestBuilder = deleteRequestBuilder(participantContextId, credentialId);

        return context.httpClient().send(requestBuilder).map(result -> credentialId);
    }

    public CompletableFuture<Result<String>> deleteAsync(String participantContextId, String credentialId) {
        var requestBuilder = deleteRequestBuilder(participantContextId, credentialId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> participantContextId));
    }

    private HttpRequest.Builder getRequestBuilder(String participantContextId, String credentialId) {
        return HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/participants/%s/credentials/%s".formatted(this.url, participantContextId, credentialId)))
                .GET();
    }

    private HttpRequest.Builder getListRequestBuilder(String participantContextId, String type) {
        return HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/participants/%s/credentials?type=%s".formatted(this.url, participantContextId, type)))
                .GET();
    }

    private HttpRequest.Builder getAllRequestBuilder(int offset, int limit) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/credentials?offset=%s&limit=%s".formatted(this.url, offset, limit)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(VerifiableCredentialManifest input, String participantContextId) {
        String requestBody = null;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/credentials".formatted(this.url, participantContextId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder updateRequestBuilder(VerifiableCredentialManifest input, String participantContextId) {
        String requestBody = null;
        try {
            requestBody = context.objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/credentials".formatted(this.url, participantContextId)))
                .header("content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private HttpRequest.Builder deleteRequestBuilder(String participantContextId, String credentialId) {
        return HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/participants/%s/credentials/%s".formatted(this.url, participantContextId, credentialId)))
                .DELETE();
    }

    private VerifiableCredentialResource getVerifiableCredential(InputStream body) {
        try {
            return context.objectMapper().readValue(body, VerifiableCredentialResource.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<VerifiableCredentialResource> getVerifiableCredentials(InputStream body) {
        try {
            return context.objectMapper().readValue(body, new TypeReference<List<VerifiableCredentialResource>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
