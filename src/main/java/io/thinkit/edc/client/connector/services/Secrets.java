package io.thinkit.edc.client.connector.services;

import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.Secret;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Secrets {
    private final String url;
    private final ManagementApiHttpClient managementApiHttpClient;

    public Secrets(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<Secret> get(String id) {
        var requestBuilder = getRequestBuilder(id);
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getSecret);
    }

    public CompletableFuture<Result<Secret>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);

        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getSecret));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/v1/secrets/%s".formatted(this.url, id)))
                .GET();
    }

    private Secret getSecret(JsonArray array) {
        return Secret.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
