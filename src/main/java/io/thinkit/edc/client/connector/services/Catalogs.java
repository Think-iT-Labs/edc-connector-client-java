package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Catalogs {
    private final String url;
    private final EdcApiHttpClient edcApiHttpClient;

    public Catalogs(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.url = "%s/v3/catalog".formatted(url);
    }

    public Result<Catalog> request(CatalogRequest input) {
        var requestBuilder = getCatalogRequestBuilder(input);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getCatalog);
    }

    public CompletableFuture<Result<Catalog>> requestAsync(CatalogRequest input) {
        var requestBuilder = getCatalogRequestBuilder(input);
        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getCatalog));
    }

    public Result<Dataset> requestDataset(DatasetRequest input) {

        var requestBuilder = requestDatasetRequestBuilder(input);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getDataset);
    }

    public CompletableFuture<Result<Dataset>> requestDatasetAsync(DatasetRequest input) {

        var requestBuilder = requestDatasetRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getDataset));
    }

    private HttpRequest.Builder requestDatasetRequestBuilder(DatasetRequest input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/dataset/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder getCatalogRequestBuilder(CatalogRequest input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private Catalog getCatalog(JsonArray array) {
        return Catalog.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Dataset getDataset(JsonArray array) {
        return Dataset.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
