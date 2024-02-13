package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ContractNegotiations extends Service {

    public ContractNegotiations(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        super(url, httpClient, interceptor);
    }

    private ContractAgreement getContractAgreement(JsonObject object) {
        return ContractAgreement.Builder.newInstance().raw(object).build();
    }

    private String getContractNegotiationState(JsonObject content) {
        return content.asJsonObject().getString("state");
    }

    public Result<ContractNegotiation> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s".formatted(getUrl(), id)))
                .GET();
        return this.send(
                requestBuilder,
                (Function<JsonObject, ContractNegotiation>) this::getContractNegotiation,
                this::getResponse);
    }

    public CompletableFuture<Result<ContractNegotiation>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s".formatted(getUrl(), id)))
                .GET();
        return this.sendAsync(
                requestBuilder,
                (Function<JsonObject, ContractNegotiation>) this::getContractNegotiation,
                this::getResponse);
    }

    public Result<String> create(ContractRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(requestBuilder, this::createResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(ContractRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.sendAsync(requestBuilder, this::createResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<ContractAgreement> getAgreement(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/agreement".formatted(getUrl(), id)))
                .GET();
        return this.send(
                requestBuilder,
                (Function<JsonObject, ContractAgreement>) this::getContractAgreement,
                this::getResponse);
    }

    public CompletableFuture<Result<ContractAgreement>> getAgreementAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/agreement".formatted(getUrl(), id)))
                .GET();
        return this.sendAsync(
                requestBuilder,
                (Function<JsonObject, ContractAgreement>) this::getContractAgreement,
                this::getResponse);
    }

    public Result<String> terminate(TerminateNegotiation input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/%s/terminate".formatted(getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(requestBuilder, input.id(), this::deleteAndUpdateResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateNegotiation input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/%s/terminate".formatted(getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.sendAsync(requestBuilder, input.id(), this::deleteAndUpdateResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<List<ContractNegotiation>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/request".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(
                    requestBuilder,
                    (Function<JsonArray, List<ContractNegotiation>>) this::getContractNegotiations,
                    this::requestResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<ContractNegotiation>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/request".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.sendAsync(
                    requestBuilder,
                    (Function<JsonArray, List<ContractNegotiation>>) this::getContractNegotiations,
                    this::requestResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> getState(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/state".formatted(getUrl(), id)))
                .GET();
        return this.send(
                requestBuilder,
                (Function<JsonObject, String>) this::getContractNegotiationState,
                this::getStatusResponse);
    }

    public CompletableFuture<Result<String>> getStateAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/state".formatted(getUrl(), id)))
                .GET();
        return this.sendAsync(
                requestBuilder,
                (Function<JsonObject, String>) this::getContractNegotiationState,
                this::getStatusResponse);
    }

    private ContractNegotiation getContractNegotiation(JsonObject object) {
        return ContractNegotiation.Builder.newInstance().raw(object).build();
    }

    private List<ContractNegotiation> getContractNegotiations(JsonArray array) {
        return array.stream()
                .map(s -> ContractNegotiation.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
