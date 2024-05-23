package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.Secret;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Secrets {
    private final String url;
    private final ManagementApiHttpClient managementApiHttpClient;

    public Secrets(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<Secret> get(String id) {
        var requestBuilder = getRequestBuilder(id);
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getSecret);
    }

    public CompletableFuture<Result<Secret>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);

        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getSecret));
    }

    public Result<String> create(Secret input) {
        var requestBuilder = createRequestBuilder(input);

        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(Secret input) {
        var requestBuilder = createRequestBuilder(input);

        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<String> update(Secret input) {
        var requestBuilder = updateRequestBuilder(input);

        return this.managementApiHttpClient.send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> updateAsync(Secret input) {

        var requestBuilder = updateRequestBuilder(input);

        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> delete(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return this.managementApiHttpClient.send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/secrets/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(Secret input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/secrets".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder updateRequestBuilder(Secret input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/secrets".formatted(this.url)))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder deleteRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/secrets/%s".formatted(this.url, id)))
                .DELETE();
    }

    private Secret getSecret(JsonArray array) {
        return Secret.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
