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
    private final ManagementApiHttpClient managementApiHttpClient;

    public Catalogs(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = "%s/v3/catalog".formatted(url);
    }

    public Result<Catalog> request(CatalogRequest input) {
        return this.managementApiHttpClient
                .send(catalogRequest(input))
                .map(JsonLdUtil::expand)
                .map(this::getCatalog);
    }

    public CompletableFuture<Result<Catalog>> requestAsync(CatalogRequest input) {
        return this.managementApiHttpClient.sendAsync(catalogRequest(input))
                .thenApply(result -> result.map(JsonLdUtil::expand).map(this::getCatalog));
    }

    public Result<Dataset> requestDataset(DatasetRequest input) {
        return this.managementApiHttpClient
                .send(datasetRequest(input))
                .map(JsonLdUtil::expand)
                .map(this::getDataset);
    }

    public CompletableFuture<Result<Dataset>> requestDatasetAsync(DatasetRequest input) {

        return this.managementApiHttpClient.sendAsync(datasetRequest(input))
                .thenApply(result -> result.map(JsonLdUtil::expand).map(this::getDataset));
    }

    private HttpRequest.Builder catalogRequest(CatalogRequest input) {
        var requestBody = compact(input);

        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder datasetRequest(DatasetRequest input) {
        var requestBody = compact(input);

        return HttpRequest.newBuilder()
                .uri(URI.create("%s/dataset/request".formatted(this.url)))
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
