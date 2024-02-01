package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
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
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class TransferProcesses {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public TransferProcesses(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    Result<TransferProcess> getResponse(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var transferProcess = TransferProcess.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(transferProcess, null);
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
            if (statusCode == 200) {
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

    Result<TransferState> getStateResponse(HttpResponse<InputStream> response) {
        try {
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var state = TransferState.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(state, null);
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

    Result<String> terminateResponse(HttpResponse<InputStream> response, String id) {
        try {
            var statusCode = response.statusCode();
            if (statusCode == 200) {
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

    Result<String> deprovisionResponse(HttpResponse<InputStream> response, String id) {
        try {
            var statusCode = response.statusCode();
            if (statusCode == 200) {
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

    public Result<TransferProcess> get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return getResponse(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<TransferProcess>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s".formatted(url, id)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(this::getResponse);
    }

    public Result<String> create(TransferRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return createResponse(response);
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(TransferRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses".formatted(url)))
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

    public Result<TransferState> getState(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return getStateResponse(response);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<TransferState>> getStateAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(url, id)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(this::getStateResponse);
    }

    public Result<String> terminate(TerminateTransfer input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/terminate".formatted(url, input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return terminateResponse(response, input.id());

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateTransfer input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/terminate".formatted(url, input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            return future.thenApply(response -> terminateResponse(response, input.id()));
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> deprovision(String id) {
        try {

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/deprovision".formatted(url, id)))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody());

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return deprovisionResponse(response, id);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> deprovisionAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/deprovision".formatted(url, id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());

        var request = interceptor.apply(requestBuilder).build();

        CompletableFuture<HttpResponse<InputStream>> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
        return future.thenApply(response -> deprovisionResponse(response, id));
    }
}
