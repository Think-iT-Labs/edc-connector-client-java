package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.deserializeToArray;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.ApiErrorDetail;
import io.thinkit.edc.client.connector.model.Result;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class EdcApiHttpClient {
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public EdcApiHttpClient(HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Result<InputStream> send(HttpRequest.Builder requestBuilder) {
        try {
            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            return toResult(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<InputStream>> sendAsync(HttpRequest.Builder requestBuilder) {
        var request = interceptor.apply(requestBuilder).build();

        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(this::toResult);
    }

    public Result<InputStream> toResult(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                return new Result<>(response.body(), null);

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

    private static boolean isSuccessful(Integer value) {
        return value >= 200 && value <= 299;
    }
}
