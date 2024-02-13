package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import io.thinkit.edc.client.connector.model.ApiErrorDetail;
import io.thinkit.edc.client.connector.model.Result;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Service {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public Service(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
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

    protected <T, B> Result<T> send(
            HttpRequest.Builder requestBuilder, B builder, BiFunction<HttpResponse<InputStream>, B, Result<T>> myFunc) {

        try {
            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return myFunc.apply(response, builder);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> Result<T> send(
            HttpRequest.Builder requestBuilder, Function<HttpResponse<InputStream>, Result<T>> myFunc) {

        try {
            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return myFunc.apply(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T, B> CompletableFuture<Result<T>> sendAsync(
            HttpRequest.Builder requestBuilder, B builder, BiFunction<HttpResponse<InputStream>, B, Result<T>> myFunc) {

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(response -> myFunc.apply(response, builder));
    }

    protected <T> CompletableFuture<Result<T>> sendAsync(
            HttpRequest.Builder requestBuilder, Function<HttpResponse<InputStream>, Result<T>> myFunc) {

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(myFunc);
    }

    protected <T> Result<T> getResponse(HttpResponse<InputStream> response, Function<JsonObject, T> build) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                JsonArray jsonArray = expand(response.body());

                var result = build.apply(jsonArray.getJsonObject(0));
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

    protected <T> Result<T> getStatusResponse(HttpResponse<InputStream> response, Function<JsonObject, T> build) {

        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                var jsonDocument = JsonDocument.of(response.body());
                var content = jsonDocument.getJsonContent().get();

                var result = build.apply(content.asJsonObject());
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

    protected Result<String> createResponse(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                var content = expand(response.body());
                var id = content.getJsonObject(0).getString(ID);
                return new Result<>(id, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(null, error);
            }
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    protected Result<String> deleteAndUpdateResponse(HttpResponse<InputStream> response, String id) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                return new Result<>(id, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(null, error);
            }
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> Result<T> requestResponse(HttpResponse<InputStream> response, Function<JsonArray, T> build) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                var jsonArray = expand(response.body());
                var result = build.apply(jsonArray);
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
