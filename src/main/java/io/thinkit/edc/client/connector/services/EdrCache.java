package io.thinkit.edc.client.connector.services;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final EdcApiHttpClient edcApiHttpClient;
    private final ObjectMapper objectMapper;

    public EdrCache(
            String url,
            HttpClient httpClient,
            UnaryOperator<HttpRequest.Builder> interceptor,
            ObjectMapper objectMapper) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.objectMapper = objectMapper;
        this.url = "%s/v3/edrs".formatted(url);
    }

    public Result<String> delete(String transferProcessId) {
        var requestBuilder = getDeleteRequestBuilder(transferProcessId);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> transferProcessId);
    }

    public CompletableFuture<Result<String>> deleteAsync(String transferProcessId) {
        var requestBuilder = getDeleteRequestBuilder(transferProcessId);

        return this.edcApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> transferProcessId));
    }

    public Result<DataAddress> dataAddress(String transferProcessId) {
        var requestBuilder = getDataAddressRequestBuilder(transferProcessId);
        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getDataAddress);
    }

    public CompletableFuture<Result<DataAddress>> dataAddressAsync(String transferProcessId) {
        var requestBuilder = getDataAddressRequestBuilder(transferProcessId);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getDataAddress));
    }

    public Result<Edr> request(QuerySpec input) {
        var requestBuilder = getRequestBuilder(input);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getEDR);
    }

    public CompletableFuture<Result<Edr>> requestAsync(QuerySpec input) {
        var requestBuilder = getRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
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
        String requestBody = null;
        try {
            requestBody = this.objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody));
    }

    private DataAddress getDataAddress(JsonArray array) {
        return DataAddress.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Edr getEDR(JsonArray array) {
        return Edr.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
