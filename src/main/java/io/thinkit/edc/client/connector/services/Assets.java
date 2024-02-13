package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Assets extends Service {
    public Assets(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        super(url, httpClient, interceptor);
    }

    public Result<Asset> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(getUrl(), id)))
                .GET();
        return this.send(requestBuilder, (Function<JsonObject, Asset>) this::getAsset, this::getResponse);
    }

    public CompletableFuture<Result<Asset>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(getUrl(), id)))
                .GET();

        return this.sendAsync(requestBuilder, (Function<JsonObject, Asset>) this::getAsset, this::getResponse);
    }

    public Result<String> create(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(requestBuilder, this::createResponse);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.sendAsync(requestBuilder, this::createResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> update(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            return this.send(requestBuilder, input.id(), this::deleteAndUpdateResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> updateAsync(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            return this.sendAsync(requestBuilder, input.id(), this::deleteAndUpdateResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(getUrl(), id)))
                .DELETE();

        return this.send(requestBuilder, id, this::deleteAndUpdateResponse);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(getUrl(), id)))
                .DELETE();

        return this.sendAsync(requestBuilder, id, this::deleteAndUpdateResponse);
    }

    public Result<List<Asset>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/request".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.send(requestBuilder, (Function<JsonArray, List<Asset>>) this::getAssets, this::requestResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<Asset>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/request".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.sendAsync(
                    requestBuilder, (Function<JsonArray, List<Asset>>) this::getAssets, this::requestResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private Asset getAsset(JsonObject object) {
        return Asset.Builder.newInstance().raw(object).build();
    }

    private List<Asset> getAssets(JsonArray array) {
        return array.stream()
                .map(s -> Asset.Builder.newInstance().raw(s.asJsonObject()).build())
                .toList();
    }
}
