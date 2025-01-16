package io.thinkit.edc.client.connector.services;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.HealthStatus;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.observability.ObservabilityResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;

public class ApplicationObservability extends ObservabilityResource {
    private final String url;

    public ApplicationObservability(EdcClientContext context) {
        super(context);
        url = "%s/check".formatted(observabilityUrl);
    }

    public Result<HealthStatus> checkHealth() {
        var requestBuilder = checkHealthRequestBuilder();

        return context.httpClient()
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkHealthAsync() {
        var requestBuilder = checkHealthRequestBuilder();

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkReadiness() {
        var requestBuilder = checkReadinessRequestBuilder();

        return context.httpClient()
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkReadinessAsync() {
        var requestBuilder = checkReadinessRequestBuilder();

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkStartup() {
        var requestBuilder = checkStartupRequestBuilder();

        return context.httpClient()
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkStartupAsync() {
        var requestBuilder = checkStartupRequestBuilder();

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus));
    }

    public Result<HealthStatus> checkLiveness() {
        var requestBuilder = checkLivenessRequestBuilder();
        return context.httpClient()
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus);
    }

    public CompletableFuture<Result<HealthStatus>> checkLivenessAsync() {
        var requestBuilder = checkLivenessRequestBuilder();
        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getHealthStatus));
    }

    private HttpRequest.Builder checkHealthRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/health".formatted(this.url)))
                .GET();
    }

    private HttpRequest.Builder checkReadinessRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/readiness".formatted(this.url)))
                .GET();
    }

    private HttpRequest.Builder checkStartupRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/startup".formatted(this.url)))
                .GET();
    }

    private HttpRequest.Builder checkLivenessRequestBuilder() {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/liveness".formatted(this.url)))
                .GET();
    }

    private HealthStatus getHealthStatus(JsonObject content) {
        return new HealthStatus(content);
    }
}
