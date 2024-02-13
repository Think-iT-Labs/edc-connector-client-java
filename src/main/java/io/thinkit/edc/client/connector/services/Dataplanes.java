package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Dataplanes extends Service {

    public Dataplanes(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        super(url, httpClient, interceptor);
    }

    public Result<List<DataPlaneInstance>> get() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/dataplanes".formatted(getUrl())))
                .GET();
        return this.send(
                requestBuilder,
                (Function<JsonArray, List<DataPlaneInstance>>) this::getDataPlaneInstances,
                this::requestResponse);
    }

    public CompletableFuture<Result<List<DataPlaneInstance>>> getAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/dataplanes".formatted(getUrl())))
                .GET();
        return this.sendAsync(
                requestBuilder,
                (Function<JsonArray, List<DataPlaneInstance>>) this::getDataPlaneInstances,
                this::requestResponse);
    }

    public Result<String> create(DataPlaneInstance input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(requestBuilder, this::createResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(DataPlaneInstance input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.sendAsync(requestBuilder, this::createResponse);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<DataPlaneInstance> select(SelectionRequest selectionRequest) {
        try {
            var requestBody = compact(selectionRequest);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes/select".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(
                    requestBuilder,
                    (Function<JsonObject, DataPlaneInstance>) this::getDataPlaneInstance,
                    this::getResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<DataPlaneInstance>> selectAsync(SelectionRequest selectionRequest) {
        try {
            var requestBody = compact(selectionRequest);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes/select".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.sendAsync(
                    requestBuilder,
                    (Function<JsonObject, DataPlaneInstance>) this::getDataPlaneInstance,
                    this::getResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private DataPlaneInstance getDataPlaneInstance(JsonObject object) {
        return DataPlaneInstance.Builder.newInstance().raw(object).build();
    }

    private List<DataPlaneInstance> getDataPlaneInstances(JsonArray array) {
        return array.stream()
                .map(s -> DataPlaneInstance.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
