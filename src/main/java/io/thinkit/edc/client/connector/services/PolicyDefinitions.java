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

public class PolicyDefinitions {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public PolicyDefinitions(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    Result<PolicyDefinition> getResponse(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                var jsonArray = expand(response.body());
                var policyDefinition = PolicyDefinition.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(policyDefinition, null);
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

    Result<String> updateResponse(HttpResponse<InputStream> response, String id) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
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

    Result<String> deleteResponse(HttpResponse<InputStream> response, String id) {
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

    Result<List<PolicyDefinition>> requestResponse(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (isSuccessful(statusCode)) {
                var jsonArray = expand(response.body());
                var policyDefinitions = jsonArray.stream()
                        .map(s -> PolicyDefinition.Builder.newInstance()
                                .raw(s.asJsonObject())
                                .build())
                        .toList();
                return new Result<>(policyDefinitions, null);
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

    public Result<PolicyDefinition> get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return getResponse(response);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<PolicyDefinition>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/policydefinitions/%s".formatted(url, id)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(this::getResponse);
    }

    public Result<String> create(PolicyDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            var request = interceptor.apply(requestBuilder).build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return createResponse(response);

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(PolicyDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions".formatted(url)))
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

    public Result<String> update(PolicyDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/%s".formatted(url, input.id())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return updateResponse(response, input.id());

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> updateAsync(PolicyDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/%s".formatted(url, input.id())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            return future.thenApply(response -> updateResponse(response, input.id()));

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/%s".formatted(url, id)))
                    .DELETE();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return deleteResponse(response, id);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/policydefinitions/%s".formatted(url, id)))
                .DELETE();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(response -> deleteResponse(response, id));
    }

    public Result<List<PolicyDefinition>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/request".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return requestResponse(response);
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<PolicyDefinition>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/request".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            return future.thenApply(this::requestResponse);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
