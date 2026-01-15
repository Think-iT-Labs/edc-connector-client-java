package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;

public class TransferProcesses extends ManagementResource {
    private final String url;

    public TransferProcesses(EdcClientContext context) {
        super(context);
        url = "%s/v3/transferprocesses".formatted(managementUrl);
    }

    public Result<TransferProcess> get(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);
        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getTransferProcess);
    }

    public CompletableFuture<Result<TransferProcess>> getAsync(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getTransferProcess));
    }

    public Result<String> create(TransferRequest input) {
        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(content -> content.getJsonObject(0)
                .getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(TransferRequest input) {

        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<TransferState> getState(String id) {
        var requestBuilder = getStateRequestBuilder(id);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getTransferState);
    }

    public CompletableFuture<Result<TransferState>> getStateAsync(String id) {
        var requestBuilder = getStateRequestBuilder(id);
        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getTransferState));
    }

    public Result<String> terminate(TerminateTransfer input) {

        var requestBuilder = terminateRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateTransfer input) {

        var requestBuilder = terminateRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> deprovision(String id) {
        var requestBuilder = deprovisionRequestBuilder(id);

        return context.httpClient().send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deprovisionAsync(String id) {
        var requestBuilder = deprovisionRequestBuilder(id);
        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    private HttpRequest.Builder getContractAgreementRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(TransferRequest input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder terminateRequestBuilder(TerminateTransfer input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/terminate".formatted(this.url, input.id())))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder getStateRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/state".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder deprovisionRequestBuilder(String id) {
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
