package io.thinkit.edc.client.connector.services;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.VerifiableCredentialResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
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

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getVerifiableCredential);
    }

    public CompletableFuture<Result<VerifiableCredentialResource>> getAsync(String participantId, String credentialId) {
        var requestBuilder = getRequestBuilder(participantId, credentialId);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getVerifiableCredential));
    }

    public Result<List<VerifiableCredentialResource>> getList(String participantId, String type) {
        var requestBuilder = getListRequestBuilder(participantId, type);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(body -> {
                    try {
                        return JsonLdUtil.deserializeToArray(body);
                    } catch (JsonLdError e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::getVerifiableCredentials);
    }

    public CompletableFuture<Result<List<VerifiableCredentialResource>>> getListAsync(
            String participantId, String type) {
        var requestBuilder = getListRequestBuilder(participantId, type);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(body -> {
                    try {
                        return JsonLdUtil.deserializeToArray(body);
                    } catch (JsonLdError e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::getVerifiableCredentials));
    }

    public Result<List<VerifiableCredentialResource>> getAll(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(body -> {
                    try {
                        return JsonLdUtil.deserializeToArray(body);
                    } catch (JsonLdError e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::getVerifiableCredentials);
    }

    public CompletableFuture<Result<List<VerifiableCredentialResource>>> getAllAsync(int offset, int limit) {
        var requestBuilder = getAllRequestBuilder(offset, limit);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(body -> {
                    try {
                        return JsonLdUtil.deserializeToArray(body);
                    } catch (JsonLdError e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::getVerifiableCredentials));
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

    private VerifiableCredentialResource getVerifiableCredential(JsonObject array) {
        return new VerifiableCredentialResource(array);
    }

    private List<VerifiableCredentialResource> getVerifiableCredentials(JsonArray array) {
        return array.stream()
                .map(s -> new VerifiableCredentialResource(s.asJsonObject()))
                .toList();
    }
}
