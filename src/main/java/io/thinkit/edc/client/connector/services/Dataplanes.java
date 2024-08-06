package io.thinkit.edc.client.connector.services;

import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import io.thinkit.edc.client.connector.model.Result;
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
    private final EdcApiHttpClient edcApiHttpClient;

    public Dataplanes(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.url = "%s/v3/dataplanes".formatted(url);
    }

    public Result<List<DataPlaneInstance>> get() {
        var requestBuilder = getRequestBuilder();

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getDataPlaneInstances);
    }

    public CompletableFuture<Result<List<DataPlaneInstance>>> getAsync() {
        var requestBuilder = getRequestBuilder();
        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getDataPlaneInstances));
    }

    private HttpRequest.Builder getRequestBuilder() {
        return HttpRequest.newBuilder().uri(URI.create(this.url)).GET();
    }

    private List<DataPlaneInstance> getDataPlaneInstances(JsonArray array) {
        return array.stream()
                .map(s -> DataPlaneInstance.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
