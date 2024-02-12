package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.HttpClientUtil.isSuccessful;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.deserializeToArray;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import io.thinkit.edc.client.connector.model.ApiErrorDetail;
import io.thinkit.edc.client.connector.model.HealthStatus;
import io.thinkit.edc.client.connector.model.Result;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ApplicationObservability {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public ApplicationObservability(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    Result<HealthStatus> getResponse(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                var jsonDocument = JsonDocument.of(response.body());
                var content = jsonDocument.getJsonContent().get();
                var healthStatus = new HealthStatus(content.asJsonObject());
                return new Result<>(healthStatus, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<HealthStatus> checkHealth() {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/check/health".formatted(url)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return getResponse(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<HealthStatus>> checkHealthAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/health".formatted(url)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(this::getResponse);
    }

    public Result<HealthStatus> checkReadiness() {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/check/readiness".formatted(url)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return getResponse(response);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<HealthStatus>> checkReadinessAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/readiness".formatted(url)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(this::getResponse);
    }

    public Result<HealthStatus> checkStartup() {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/check/startup".formatted(url)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return getResponse(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<HealthStatus>> checkStartupAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/startup".formatted(url)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(this::getResponse);
    }

    public Result<HealthStatus> checkLiveness() {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/check/liveness".formatted(url)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return getResponse(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<HealthStatus>> checkLivenessAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/check/liveness".formatted(url)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(this::getResponse);
    }
}
