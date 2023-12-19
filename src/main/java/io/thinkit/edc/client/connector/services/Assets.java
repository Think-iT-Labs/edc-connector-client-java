package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.ApiErrorDetail;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;

public class Assets {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public Assets(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Result<Asset> get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            var response = future.get();
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var asset = Asset.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(asset, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }
        } catch (InterruptedException | JsonLdError | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> create(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            var response = future.get();
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var content = expand(response.body());
                var id = content.getJsonObject(0).getString(ID);
                return new Result<>(id, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(null, error);
            }

        } catch (InterruptedException | JsonLdError | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> update(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(url)))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            var response = future.get();
            var statusCode = response.statusCode();
            if (statusCode == 204) {
                return new Result<>(input.id(), null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(null, error);
            }

        } catch (ExecutionException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/%s".formatted(url, id)))
                    .DELETE();

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            var response = future.get();
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                return new Result<>(id, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(null, error);
            }
        } catch (ExecutionException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<List<Asset>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/request".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            var response = future.get();
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var assets = jsonArray.stream()
                        .map(s -> Asset.Builder.newInstance()
                                .raw(s.asJsonObject())
                                .build())
                        .toList();
                return new Result<>(assets, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }

        } catch (ExecutionException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
