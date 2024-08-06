package io.thinkit.edc.client.connector.services;

import io.thinkit.edc.client.connector.model.HealthStatus;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ApplicationObservability {
    private final String url;
    private final EdcApiHttpClient edcApiHttpClient;

    public ApplicationObservability(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<HealthStatus> checkHealth() {
        var requestBuilder = checkHealthRequestBuilder();

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkHealthAsync() {
        var requestBuilder = checkHealthRequestBuilder();

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkReadiness() {
        var requestBuilder = checkReadinessRequestBuilder();

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkReadinessAsync() {
        var requestBuilder = checkReadinessRequestBuilder();

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkStartup() {
        var requestBuilder = checkStartupRequestBuilder();

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkStartupAsync() {
        var requestBuilder = checkStartupRequestBuilder();

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkLiveness() {
        var requestBuilder = checkLivenessRequestBuilder();
        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkLivenessAsync() {
        var requestBuilder = checkLivenessRequestBuilder();
        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus));
    }

    private HttpRequest.Builder checkHealthRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/check/health".formatted(this.url)))
                .GET();
    }

    private HttpRequest.Builder checkReadinessRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/check/readiness".formatted(this.url)))
                .GET();
    }

    private HttpRequest.Builder checkStartupRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/check/startup".formatted(this.url)))
                .GET();
    }

    private HttpRequest.Builder checkLivenessRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/check/liveness".formatted(this.url)))
                .GET();
    }

    private HealthStatus getHealthStatus(JsonObject content) {
        return new HealthStatus(content);
    }
}
