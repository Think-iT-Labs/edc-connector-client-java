package io.thinkit.edc.client.connector.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.VerifiableCredentialManifest;
import io.thinkit.edc.client.connector.model.VerifiableCredentialResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class VerifiableCredentials {
    private final String url;
    private final EdcApiHttpClient edcApiHttpClient;

    public VerifiableCredentials(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.url = "%s/v1alpha".formatted(url);
    }

    public Result<VerifiableCredentialResource> get(String participantId, String credentialId) {
        var requestBuilder = getRequestBuilder(participantId, credentialId);

        return this.edcApiHttpClient.send(requestBuilder).map(this::getVerifiableCredential);
    }

    public CompletableFuture<Result<VerifiableCredentialResource>> getAsync(String participantId, String credentialId) {
        var requestBuilder = getRequestBuilder(participantId, credentialId);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(this::getVerifiableCredential));
    }

    public Result<List<VerifiableCredentialResource>> getList(String participantId, String type) {
        var requestBuilder = getListRequestBuilder(participantId, type);

        return this.edcApiHttpClient.send(requestBuilder).map(this::getVerifiableCredentials);
    }

    public CompletableFuture<Result<List<VerifiableCredentialResource>>> getListAsync(
            String participantId, String type) {
        var requestBuilder = getListRequestBuilder(participantId, type);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(this::getVerifiableCredentials));
    }

    public Result<List<VerifiableCredentialResource>> getAll(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return this.edcApiHttpClient.send(requestBuilder).map(this::getVerifiableCredentials);
    }

    public CompletableFuture<Result<List<VerifiableCredentialResource>>> getAllAsync(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(this::getVerifiableCredentials));
    }

    public Result<String> create(VerifiableCredentialManifest input, String participantId) {
        var requestBuilder = createRequestBuilder(input, participantId);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> createAsync(VerifiableCredentialManifest input, String participantId) {
        var requestBuilder = createRequestBuilder(input, participantId);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    private HttpRequest.Builder getRequestBuilder(String participantId, String credentialId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/credentials/%s".formatted(this.url, participantId, credentialId)))
                .GET();
    }

    private HttpRequest.Builder getListRequestBuilder(String participantId, String type) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/credentials?type=%s".formatted(this.url, participantId, type)))
                .GET();
    }

    private HttpRequest.Builder getAllRequestBuilder(int offset, int limit) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/credentials?offset=%s&limit=%s".formatted(this.url, offset, limit)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(VerifiableCredentialManifest input, String participantId) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/participants/%s/credentials".formatted(this.url, participantId)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));
    }

    private VerifiableCredentialResource getVerifiableCredential(InputStream body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(body, VerifiableCredentialResource.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getVerifiableCredential2(InputStream body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            var res = objectMapper.readValue(body, Object.class);
            System.out.println(res);
            return "ok";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<VerifiableCredentialResource> getVerifiableCredentials(InputStream body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(body, new TypeReference<List<VerifiableCredentialResource>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
