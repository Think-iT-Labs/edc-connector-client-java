package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Dataplanes {
    private final ManagementApiHttpClient managementApiHttpClient;

    public Dataplanes(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(url, httpClient, interceptor);
    }

    public Result<List<DataPlaneInstance>> get() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/dataplanes".formatted(managementApiHttpClient.getUrl())))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "get", this::getDataPlaneInstances);
    }

    public CompletableFuture<Result<List<DataPlaneInstance>>> getAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/dataplanes".formatted(managementApiHttpClient.getUrl())))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getDataPlaneInstances);
    }

    public Result<String> create(DataPlaneInstance input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "");

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(DataPlaneInstance input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, "");
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<DataPlaneInstance> select(SelectionRequest selectionRequest) {
        try {
            var requestBody = compact(selectionRequest);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes/select".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "get", this::getDataPlaneInstance);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<DataPlaneInstance>> selectAsync(SelectionRequest selectionRequest) {
        try {
            var requestBody = compact(selectionRequest);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes/select".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getDataPlaneInstance);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private DataPlaneInstance getDataPlaneInstance(JsonArray array) {
        return DataPlaneInstance.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private List<DataPlaneInstance> getDataPlaneInstances(JsonArray array) {
        return array.stream()
                .map(s -> DataPlaneInstance.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
