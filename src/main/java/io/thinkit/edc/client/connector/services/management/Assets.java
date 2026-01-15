package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Assets extends ManagementResource {
    private final String url;

    public Assets(EdcClientContext context) {
        super(context);
        url = "%s/v3/assets".formatted(managementUrl);
    }

    public Result<Asset> get(String id) {
        var requestBuilder = getRequestBuilder(id);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getAsset);
    }

    public CompletableFuture<Result<Asset>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getAsset));
    }

    public Result<String> create(Asset input) {
        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(content -> content.getJsonObject(0)
                .getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(Asset input) {
        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<String> update(Asset input) {
        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> updateAsync(Asset input) {

        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> delete(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return context.httpClient().send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    public Result<List<Asset>> request(QuerySpec input) {

        var requestBuilder = getAssetsRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getAssets);
    }

    public CompletableFuture<Result<List<Asset>>> requestAsync(QuerySpec input) {
        var requestBuilder = getAssetsRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getAssets));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(Asset input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder updateRequestBuilder(Asset input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder deleteRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .DELETE();
    }

    private HttpRequest.Builder getAssetsRequestBuilder(QuerySpec input) {
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
