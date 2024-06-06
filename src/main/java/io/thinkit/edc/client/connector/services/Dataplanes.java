package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Dataplanes {
    private final String url;
    private final ManagementApiHttpClient managementApiHttpClient;

    public Dataplanes(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<List<DataPlaneInstance>> get() {
        var requestBuilder = getRequestBuilder();

        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getDataPlaneInstances);
    }

    public CompletableFuture<Result<List<DataPlaneInstance>>> getAsync() {
        var requestBuilder = getRequestBuilder();
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getDataPlaneInstances));
    }

    public Result<String> create(DataPlaneInstance input) {

        var requestBuilder = createRequestBuilder(input);

        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(DataPlaneInstance input) {

        var requestBuilder = createRequestBuilder(input);

        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<DataPlaneInstance> select(SelectionRequest selectionRequest) {

        var requestBuilder = selectRequestBuilder(selectionRequest);

        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getDataPlaneInstance);
    }

    public CompletableFuture<Result<DataPlaneInstance>> selectAsync(SelectionRequest selectionRequest) {

        var requestBuilder = selectRequestBuilder(selectionRequest);

        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getDataPlaneInstance));
    }

    private HttpRequest.Builder getRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/dataplanes".formatted(this.url)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(DataPlaneInstance input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/dataplanes".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder selectRequestBuilder(SelectionRequest input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/dataplanes/select".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
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
