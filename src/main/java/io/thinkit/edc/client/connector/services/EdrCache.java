package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class EdrCache {
    private final String url;
    private final ManagementApiHttpClient managementApiHttpClient;

    public EdrCache(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<String> delete(String transferProcessId) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/edrs/%s".formatted(this.url, transferProcessId)))
                .DELETE();

        return this.managementApiHttpClient.send(requestBuilder).map(result -> transferProcessId);
    }

    public CompletableFuture<Result<String>> deleteAsync(String transferProcessId) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/edrs/%s".formatted(this.url, transferProcessId)))
                .DELETE();

        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> transferProcessId));
    }

    public Result<DataAddress> dataAddress(String transferProcessId) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/edrs/%s/dataaddress".formatted(this.url, transferProcessId)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getDataAddress);
    }

    public CompletableFuture<Result<DataAddress>> dataAddressAsync(String transferProcessId) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/edrs/%s/dataaddress".formatted(this.url, transferProcessId)))
                .GET();

        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getDataAddress));
    }

    public Result<Edr> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v1/edrs/request".formatted(this.url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient
                    .send(requestBuilder)
                    .map(JsonLdUtil::expand)
                    .map(this::getEDR);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<Edr>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v1/edrs/request".formatted(this.url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient
                    .sendAsync(requestBuilder)
                    .thenApply(result -> result.map(JsonLdUtil::expand).map(this::getEDR));

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private DataAddress getDataAddress(JsonArray array) {
        return DataAddress.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Edr getEDR(JsonArray array) {
        return Edr.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
