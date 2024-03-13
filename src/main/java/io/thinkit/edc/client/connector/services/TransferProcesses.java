package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class TransferProcesses {
    private final String url;
    private final ManagementApiHttpClient managementApiHttpClient;

    public TransferProcesses(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<TransferProcess> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getTransferProcess);
    }

    public CompletableFuture<Result<TransferProcess>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getTransferProcess));
    }

    public Result<String> create(TransferRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses".formatted(this.url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient
                    .send(requestBuilder)
                    .map(JsonLdUtil::expand)
                    .map(content -> content.getJsonObject(0).getString(ID));
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(TransferRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses".formatted(this.url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(
                            JsonLdUtil::expand)
                    .map(content -> content.getJsonObject(0).getString(ID)));
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<TransferState> getState(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(this.url, id)))
                .GET();

        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getTransferState);
    }

    public CompletableFuture<Result<TransferState>> getStateAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getTransferState));
    }

    public Result<String> terminate(TerminateTransfer input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/terminate".formatted(this.url, input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.send(requestBuilder).map(result -> input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateTransfer input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/terminate".formatted(this.url, input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient
                    .sendAsync(requestBuilder)
                    .thenApply(result -> result.map(content -> input.id()));
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> deprovision(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/deprovision".formatted(this.url, id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());

        return this.managementApiHttpClient.send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deprovisionAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/deprovision".formatted(this.url, id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    private TransferProcess getTransferProcess(JsonArray array) {
        return TransferProcess.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private TransferState getTransferState(JsonArray array) {
        return TransferState.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
