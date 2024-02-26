package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Assets {
    private final ManagementApiHttpClient managementApiHttpClient;

    public Assets(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(url, httpClient, interceptor);
    }

    public Result<Asset> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "get", this::getAsset);
    }

    public CompletableFuture<Result<Asset>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();

        return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getAsset);
    }

    public Result<String> create(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "");
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, "");

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> update(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            return this.managementApiHttpClient.send(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> updateAsync(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .DELETE();

        return this.managementApiHttpClient.send(requestBuilder, id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .DELETE();

        return this.managementApiHttpClient.sendAsync(requestBuilder, id);
    }

    public Result<List<Asset>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.send(requestBuilder, "get", this::getAssets);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<Asset>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getAssets);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
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
