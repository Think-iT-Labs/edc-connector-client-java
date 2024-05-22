package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ContractNegotiations {
    private final String url;
    private final ManagementApiHttpClient managementApiHttpClient;

    public ContractNegotiations(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<ContractNegotiation> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractNegotiation);
    }

    public CompletableFuture<Result<ContractNegotiation>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractNegotiation));
    }

    public Result<String> create(ContractRequest input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(ContractRequest input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<ContractAgreement> getAgreement(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/agreement".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractAgreement);
    }

    public CompletableFuture<Result<ContractAgreement>> getAgreementAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/agreement".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractAgreement));
    }

    public Result<String> terminate(TerminateNegotiation input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/terminate".formatted(this.url, input.id())))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient.send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateNegotiation input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/terminate".formatted(this.url, input.id())))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));

        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> input.id()));
    }

    public Result<List<ContractNegotiation>> request(QuerySpec input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractNegotiations);
    }

    public CompletableFuture<Result<List<ContractNegotiation>>> requestAsync(QuerySpec input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractNegotiations));
    }

    public Result<String> getState(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/state".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getContractNegotiationState);
    }

    public CompletableFuture<Result<String>> getStateAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/state".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(JsonLdUtil::ToJsonObject).map(this::getContractNegotiationState));
    }

    private ContractNegotiation getContractNegotiation(JsonArray array) {
        return ContractNegotiation.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private ContractAgreement getContractAgreement(JsonArray array) {
        return ContractAgreement.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private String getContractNegotiationState(JsonObject content) {
        return content.asJsonObject().getString("state");
    }

    private List<ContractNegotiation> getContractNegotiations(JsonArray array) {
        return array.stream()
                .map(s -> ContractNegotiation.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
