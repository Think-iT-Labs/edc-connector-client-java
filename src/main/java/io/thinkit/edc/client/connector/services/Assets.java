package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Assets {
    private final String url;
    private final ManagementApiHttpClient managementApiHttpClient;

    public Assets(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = "%s/v3/assets".formatted(url);
    }

    public Result<Asset> get(String id) {
        return this.managementApiHttpClient
                .send(getRequest(id))
                .map(JsonLdUtil::expand)
                .map(this::getAsset);
    }

    public CompletableFuture<Result<Asset>> getAsync(String id) {
        return this.managementApiHttpClient
                .sendAsync(getRequest(id))
                .thenApply(result -> result.map(JsonLdUtil::expand)
                    .map(this::getAsset)
                );
    }

    public Result<String> create(Asset input) {
        return this.managementApiHttpClient
                .send(createRequest(input))
                .map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(Asset input) {
        return this.managementApiHttpClient
                .sendAsync(createRequest(input))
                .thenApply(result -> result.map(JsonLdUtil::expand)
                    .map(content -> content.getJsonObject(0).getString(ID))
                );
    }

    public Result<String> update(Asset input) {
        return this.managementApiHttpClient
                .send(updateRequest(input))
                .map(result -> input.id());
    }

    public CompletableFuture<Result<String>> updateAsync(Asset input) {
        return this.managementApiHttpClient
                .sendAsync(updateRequest(input))
                .thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> delete(String id) {
        return this.managementApiHttpClient
                .send(deleteRequest(id))
                .map(result -> id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        return this.managementApiHttpClient
                .sendAsync(deleteRequest(id))
                .thenApply(result -> result.map(content -> id));
    }

    public Result<List<Asset>> request(QuerySpec input) {
        return this.managementApiHttpClient
                .send(queryRequest(input))
                .map(JsonLdUtil::expand)
                .map(this::getAssets);
    }

    public CompletableFuture<Result<List<Asset>>> requestAsync(QuerySpec input) {
        return this.managementApiHttpClient.sendAsync(queryRequest(input))
                .thenApply(result -> result.map(JsonLdUtil::expand).map(this::getAssets));
    }

    private HttpRequest.Builder getRequest(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequest(Asset input) {
        var requestBody = compact(input);

        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder updateRequest(Asset input) {
        var requestBody = compact(input);

        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder deleteRequest(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .DELETE();
    }

    private HttpRequest.Builder queryRequest(QuerySpec input) {
        var requestBody = compact(input);

        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private Asset getAsset(JsonArray array) {
        return Asset.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private List<Asset> getAssets(JsonArray array) {
        return array.stream()
                .map(s -> Asset.Builder.newInstance().raw(s.asJsonObject()).build())
                .toList();
    }
}
