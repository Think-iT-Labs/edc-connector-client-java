package io.thinkit.edc.client.connector.services;

import io.thinkit.edc.client.connector.model.HealthStatus;
import io.thinkit.edc.client.connector.model.Result;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ApplicationObservability {
    private final ManagementApiHttpClient managementApiHttpClient;

    public ApplicationObservability(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(url, httpClient, interceptor);
    }

    public Result<HealthStatus> checkHealth() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/health".formatted(managementApiHttpClient.getUrl())))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "getStatus", this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkHealthAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/health".formatted(managementApiHttpClient.getUrl())))
                .GET();

        return this.managementApiHttpClient.sendAsync(requestBuilder, "getStatus", this::getHealthStatus);
    }

    public Result<HealthStatus> checkReadiness() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/readiness".formatted(managementApiHttpClient.getUrl())))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "getStatus", this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkReadinessAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/readiness".formatted(managementApiHttpClient.getUrl())))
                .GET();

        return this.managementApiHttpClient.sendAsync(requestBuilder, "getStatus", this::getHealthStatus);
    }

    public Result<HealthStatus> checkStartup() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/startup".formatted(managementApiHttpClient.getUrl())))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "getStatus", this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkStartupAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/startup".formatted(managementApiHttpClient.getUrl())))
                .GET();

        return this.managementApiHttpClient.sendAsync(requestBuilder, "getStatus", this::getHealthStatus);
    }

    public Result<HealthStatus> checkLiveness() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/liveness".formatted(managementApiHttpClient.getUrl())))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "getStatus", this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkLivenessAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/liveness".formatted(managementApiHttpClient.getUrl())))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "getStatus", this::getHealthStatus);
    }

    private HealthStatus getHealthStatus(JsonObject content) {
        return new HealthStatus(content);
    }
}
