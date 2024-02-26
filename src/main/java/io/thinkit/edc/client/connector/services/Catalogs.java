package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Catalogs {
    private final ManagementApiHttpClient managementApiHttpClient;

    public Catalogs(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(url, httpClient, interceptor);
    }

    public Result<Catalog> request(CatalogRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/catalog/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.send(requestBuilder, "get", this::getCatalog);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<Catalog>> requestAsync(CatalogRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/catalog/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getCatalog);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<Dataset> requestDataset(DatasetRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/catalog/dataset/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.send(requestBuilder, "get", this::getDataset);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<Dataset>> requestDatasetAsync(DatasetRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/catalog/dataset/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getDataset);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private Catalog getCatalog(JsonArray array) {
        return Catalog.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Dataset getDataset(JsonArray array) {
        return Dataset.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
