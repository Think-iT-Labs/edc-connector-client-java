package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class TransferProcesses {
    private final ManagementApiHttpClient managementApiHttpClient;

    public TransferProcesses(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(url, httpClient, interceptor);
    }

    public Result<TransferProcess> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "get", this::getTransferProcess);
    }

    public CompletableFuture<Result<TransferProcess>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getTransferProcess);
    }

    public Result<String> create(TransferRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "");
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(TransferRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.sendAsync(requestBuilder, "");
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<TransferState> getState(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();

        return this.managementApiHttpClient.send(requestBuilder, "get", this::getTransferState);
    }

    public CompletableFuture<Result<TransferState>> getStateAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getTransferState);
    }

    public Result<String> terminate(TerminateTransfer input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/terminate"
                            .formatted(managementApiHttpClient.getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.send(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateTransfer input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/terminate"
                            .formatted(managementApiHttpClient.getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> deprovision(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/v2/transferprocesses/%s/deprovision".formatted(managementApiHttpClient.getUrl(), id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());

        return this.managementApiHttpClient.send(requestBuilder, id);
    }

    public CompletableFuture<Result<String>> deprovisionAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/v2/transferprocesses/%s/deprovision".formatted(managementApiHttpClient.getUrl(), id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());
        return this.managementApiHttpClient.sendAsync(requestBuilder, id);
    }

    private TransferProcess getTransferProcess(JsonArray array) {
        return TransferProcess.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private TransferState getTransferState(JsonArray array) {
        return TransferState.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
