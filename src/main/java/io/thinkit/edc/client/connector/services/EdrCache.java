package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.DataAddress;
import io.thinkit.edc.client.connector.model.Edr;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;

public class EdrCache extends ManagementResource {
    private final String url;

    public EdrCache(EdcClientContext context) {
        super(context);
        url = "%s/v3/edrs".formatted(managementUrl);
    }

    public Result<String> delete(String transferProcessId) {
        var requestBuilder = getDeleteRequestBuilder(transferProcessId);

        return context.httpClient().send(requestBuilder).map(result -> transferProcessId);
    }

    public CompletableFuture<Result<String>> deleteAsync(String transferProcessId) {
        var requestBuilder = getDeleteRequestBuilder(transferProcessId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> transferProcessId));
    }

    public Result<DataAddress> dataAddress(String transferProcessId) {
        var requestBuilder = getDataAddressRequestBuilder(transferProcessId);
        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getDataAddress);
    }

    public CompletableFuture<Result<DataAddress>> dataAddressAsync(String transferProcessId) {
        var requestBuilder = getDataAddressRequestBuilder(transferProcessId);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getDataAddress));
    }

    public Result<Edr> request(QuerySpec input) {
        var requestBuilder = getRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getEDR);
    }

    public CompletableFuture<Result<Edr>> requestAsync(QuerySpec input) {
        var requestBuilder = getRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getEDR));
    }

    private HttpRequest.Builder getDeleteRequestBuilder(String transferProcessId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, transferProcessId)))
                .DELETE();
    }

    private HttpRequest.Builder getDataAddressRequestBuilder(String transferProcessId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/dataaddress".formatted(this.url, transferProcessId)))
                .GET();
    }

    private HttpRequest.Builder getRequestBuilder(QuerySpec input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private DataAddress getDataAddress(JsonArray array) {
        return DataAddress.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Edr getEDR(JsonArray array) {
        return Edr.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
