package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

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
        this.url = "%s/v3/transferprocesses".formatted(url);
    }

    public Result<TransferProcess> get(String id) {
        return this.managementApiHttpClient
                .send(getRequest(id))
                .map(JsonLdUtil::expand)
                .map(this::getTransferProcess);
    }

    public CompletableFuture<Result<TransferProcess>> getAsync(String id) {
        return this.managementApiHttpClient.sendAsync(getRequest(id)).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getTransferProcess));
    }

    public Result<String> create(TransferRequest input) {
        return this.managementApiHttpClient
                .send(createRequest(input))
                .map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(TransferRequest input) {
        return this.managementApiHttpClient.sendAsync(createRequest(input)).thenApply(result -> result.map(
                        JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<TransferState> getState(String id) {
        return this.managementApiHttpClient
                .send(getStateRequest(id))
                .map(JsonLdUtil::expand)
                .map(this::getTransferState);
    }

    public CompletableFuture<Result<TransferState>> getStateAsync(String id) {
        return this.managementApiHttpClient
                .sendAsync(getStateRequest(id))
                .thenApply(result -> result.map(JsonLdUtil::expand).map(this::getTransferState));
    }

    public Result<String> terminate(TerminateTransfer input) {
        return this.managementApiHttpClient.send(terminateRequest(input)).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateTransfer input) {
        return this.managementApiHttpClient
                .sendAsync(terminateRequest(input))
                .thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> deprovision(String id) {
        return this.managementApiHttpClient.send(deprovisionRequest(id)).map(result -> id);
    }

    public CompletableFuture<Result<String>> deprovisionAsync(String id) {
        return this.managementApiHttpClient
                .sendAsync(deprovisionRequest(id))
                .thenApply(result -> result.map(content -> id));
    }

    private HttpRequest.Builder getRequest(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequest(TransferRequest input) {
        var requestBody = compact(input);

        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder getStateRequest(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/state".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder terminateRequest(TerminateTransfer input) {
        var requestBody = compact(input);

        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/terminate".formatted(this.url, input.id())))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder deprovisionRequest(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/deprovision".formatted(this.url, id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());
    }

    private TransferProcess getTransferProcess(JsonArray array) {
        return TransferProcess.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private TransferState getTransferState(JsonArray array) {
        return TransferState.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }
}
