package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.HttpClientUtil.isSuccessful;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class Dataplanes {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public Dataplanes(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    Result<List<DataPlaneInstance>> getResponse(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                var jsonArray = expand(response.body());
                var dataplanes = jsonArray.stream()
                        .map(s -> DataPlaneInstance.Builder.newInstance()
                                .raw(s.asJsonObject())
                                .build())
                        .toList();
                return new Result<>(dataplanes, null);
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

    Result<String> createResponse(HttpResponse<InputStream> response) {
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
                return new Result<>(error);
            }
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    Result<DataPlaneInstance> selectResponse(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                var jsonArray = expand(response.body());
                var dataplane = DataPlaneInstance.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(dataplane, null);
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

    public Result<List<DataPlaneInstance>> get() {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(url)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return getResponse(response);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<DataPlaneInstance>>> getAsync() {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/dataplanes".formatted(url)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(this::getResponse);
    }

    public Result<String> create(DataPlaneInstance input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return createResponse(response);
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(DataPlaneInstance input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            return future.thenApply(this::createResponse);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<DataPlaneInstance> select(SelectionRequest selectionRequest) {
        try {
            var requestBody = compact(selectionRequest);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes/select".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return selectResponse(response);
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<DataPlaneInstance>> selectAsync(SelectionRequest selectionRequest) {
        try {
            var requestBody = compact(selectionRequest);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes/select".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();
            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            return future.thenApply(this::selectResponse);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
