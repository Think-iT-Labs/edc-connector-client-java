package io.thinkit.edc.client.connector.services;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Dataplanes extends ManagementResource {
    private final String url;

    public Dataplanes(EdcClientContext context) {
        super(context);
        url = "%s/v3/dataplanes".formatted(managementUrl);
    }

    public Result<List<DataPlaneInstance>> get() {
        var requestBuilder = getRequestBuilder();

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getDataPlaneInstances);
    }

    public CompletableFuture<Result<List<DataPlaneInstance>>> getAsync() {
        var requestBuilder = getRequestBuilder();
        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
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
