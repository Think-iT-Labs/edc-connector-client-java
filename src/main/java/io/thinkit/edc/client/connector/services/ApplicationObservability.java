package io.thinkit.edc.client.connector.services;

import io.thinkit.edc.client.connector.model.HealthStatus;
import io.thinkit.edc.client.connector.model.Result;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ApplicationObservability extends Service {
    public ApplicationObservability(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        super(url, httpClient, interceptor);
    }

    public Result<HealthStatus> checkHealth() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/health".formatted(getUrl())))
                .GET();
        return this.send(
                requestBuilder, (Function<JsonObject, HealthStatus>) this::getHealthStatus, this::getStatusResponse);
    }

    public CompletableFuture<Result<HealthStatus>> checkHealthAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/health".formatted(getUrl())))
                .GET();

        return this.sendAsync(
                requestBuilder, (Function<JsonObject, HealthStatus>) this::getHealthStatus, this::getStatusResponse);
    }

    public Result<HealthStatus> checkReadiness() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/readiness".formatted(getUrl())))
                .GET();
        return this.send(
                requestBuilder, (Function<JsonObject, HealthStatus>) this::getHealthStatus, this::getStatusResponse);
    }

    public CompletableFuture<Result<HealthStatus>> checkReadinessAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/readiness".formatted(getUrl())))
                .GET();

        return this.sendAsync(
                requestBuilder, (Function<JsonObject, HealthStatus>) this::getHealthStatus, this::getStatusResponse);
    }

    public Result<HealthStatus> checkStartup() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/startup".formatted(getUrl())))
                .GET();
        return this.send(
                requestBuilder, (Function<JsonObject, HealthStatus>) this::getHealthStatus, this::getStatusResponse);
    }

    public CompletableFuture<Result<HealthStatus>> checkStartupAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/startup".formatted(getUrl())))
                .GET();

        return this.sendAsync(
                requestBuilder, (Function<JsonObject, HealthStatus>) this::getHealthStatus, this::getStatusResponse);
    }

    public Result<HealthStatus> checkLiveness() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/liveness".formatted(getUrl())))
                .GET();
        return this.send(
                requestBuilder, (Function<JsonObject, HealthStatus>) this::getHealthStatus, this::getStatusResponse);
    }

    public CompletableFuture<Result<HealthStatus>> checkLivenessAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/liveness".formatted(getUrl())))
                .GET();
        return this.sendAsync(
                requestBuilder, (Function<JsonObject, HealthStatus>) this::getHealthStatus, this::getStatusResponse);
    }

    private HealthStatus getHealthStatus(JsonObject content) {
        return new HealthStatus(content);
    }
}
