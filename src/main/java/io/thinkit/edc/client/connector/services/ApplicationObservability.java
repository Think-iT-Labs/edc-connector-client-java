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
    private final ManagementApiHttpClient managementApiHttpClient;

    public ApplicationObservability(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<HealthStatus> checkHealth() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/health".formatted(this.url)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkHealthAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/health".formatted(this.url)))
                .GET();

        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(JsonLdUtil::ToJsonObject).map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkReadiness() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/readiness".formatted(this.url)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkReadinessAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/readiness".formatted(this.url)))
                .GET();

        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(JsonLdUtil::ToJsonObject).map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkStartup() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/startup".formatted(this.url)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkStartupAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/startup".formatted(this.url)))
                .GET();

        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(JsonLdUtil::ToJsonObject).map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkLiveness() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/liveness".formatted(this.url)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkLivenessAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/liveness".formatted(this.url)))
                .GET();
        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(JsonLdUtil::ToJsonObject).map(this::getHealthStatus));
    }

    private HealthStatus getHealthStatus(JsonObject content) {
        return new HealthStatus(content);
    }
}
