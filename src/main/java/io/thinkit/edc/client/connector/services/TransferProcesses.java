package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TransferProcesses extends Service {

    public TransferProcesses(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        super(url, httpClient, interceptor);
    }

    public Result<TransferProcess> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s".formatted(getUrl(), id)))
                .GET();
        return this.send(
                requestBuilder, (Function<JsonObject, TransferProcess>) this::getTransferProcess, this::getResponse);
    }

    public CompletableFuture<Result<TransferProcess>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s".formatted(getUrl(), id)))
                .GET();
        return this.sendAsync(
                requestBuilder, (Function<JsonObject, TransferProcess>) this::getTransferProcess, this::getResponse);
    }

    public Result<String> create(TransferRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(requestBuilder, this::createResponse);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(TransferRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.sendAsync(requestBuilder, this::createResponse);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<TransferState> getState(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(getUrl(), id)))
                .GET();

        return this.send(
                requestBuilder, (Function<JsonObject, TransferState>) this::getTransferState, this::getResponse);
    }

    public CompletableFuture<Result<TransferState>> getStateAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(getUrl(), id)))
                .GET();
        return this.sendAsync(
                requestBuilder, (Function<JsonObject, TransferState>) this::getTransferState, this::getResponse);
    }

    public Result<String> terminate(TerminateTransfer input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/terminate".formatted(getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.send(requestBuilder, input.id(), this::deleteAndUpdateResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateTransfer input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/terminate".formatted(getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.sendAsync(requestBuilder, input.id(), this::deleteAndUpdateResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> deprovision(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/deprovision".formatted(getUrl(), id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());

        return this.send(requestBuilder, id, this::deleteAndUpdateResponse);
    }

    public CompletableFuture<Result<String>> deprovisionAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/transferprocesses/%s/deprovision".formatted(getUrl(), id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());
        return this.sendAsync(requestBuilder, id, this::deleteAndUpdateResponse);
    }

    private TransferProcess getTransferProcess(JsonObject object) {
        return TransferProcess.Builder.newInstance().raw(object).build();
    }

    private TransferState getTransferState(JsonObject object) {
        return TransferState.Builder.newInstance().raw(object).build();
    }
}
