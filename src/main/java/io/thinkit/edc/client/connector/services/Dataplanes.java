package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.ApiErrorDetail;
import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.SelectionRequest;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    public Result<List<DataPlaneInstance>> get() {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(url)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            var response = future.get();
            var statusCode = response.statusCode();
            if (statusCode == 200) {
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
        } catch (ExecutionException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> create(DataPlaneInstance input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/dataplanes".formatted(url)))
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
                return new Result<>(error);
            }

        } catch (ExecutionException | InterruptedException | JsonLdError e) {
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

            CompletableFuture<HttpResponse<InputStream>> future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());
            var response = future.get();
            var statusCode = response.statusCode();
            if (statusCode == 200) {
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

        } catch (ExecutionException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
