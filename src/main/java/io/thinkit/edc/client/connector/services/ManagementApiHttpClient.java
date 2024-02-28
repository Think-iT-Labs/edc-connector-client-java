package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.deserializeToArray;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.expand;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import io.thinkit.edc.client.connector.model.ApiErrorDetail;
import io.thinkit.edc.client.connector.model.Result;
import jakarta.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ManagementApiHttpClient {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public ManagementApiHttpClient(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public String getUrl() {
        return url;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public UnaryOperator<HttpRequest.Builder> getInterceptor() {
        return interceptor;
    }

    protected <T, B> Result<T> send(HttpRequest.Builder requestBuilder, String method, Function<B, T> build) {

        try {
            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            return get(response, method, build);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected Result<String> send(HttpRequest.Builder requestBuilder, String id) {

        try {
            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            return get(response, id);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected Result<InputStream> send(HttpRequest.Builder requestBuilder) {
        try {
            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            return get(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T, B> CompletableFuture<Result<T>> sendAsync(
            HttpRequest.Builder requestBuilder, String method, Function<B, T> build) {

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(response -> get(response, method, build));
    }

    protected CompletableFuture<Result<String>> sendAsync(HttpRequest.Builder requestBuilder, String id) {

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(response -> get(response, id));
    }

    protected Result<InputStream> get(HttpResponse<InputStream> response) {
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

    protected Result<String> get(HttpResponse<InputStream> response, String id) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                if (id.isEmpty()) {
                    var content = expand(response.body());
                    var result = content.getJsonObject(0).getString(ID);
                    return new Result<>(result, null);
                }
                return new Result<>(id, null);

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

    protected <T, B> Result<T> get(HttpResponse<InputStream> response, String method, Function<B, T> build) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                T result = null;
                switch (method) {
                    case "get" -> {
                        JsonArray jsonArray = expand(response.body());
                        result = build.apply((B) jsonArray);
                    }
                    case "getStatus" -> {
                        var jsonDocument = JsonDocument.of(response.body());
                        var content = jsonDocument.getJsonContent().get();

                        result = build.apply((B) content.asJsonObject());
                    }
                    default -> {
                        return new Result<T>((T) null, null);
                    }
                }

                return new Result<T>((T) result, null);

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
