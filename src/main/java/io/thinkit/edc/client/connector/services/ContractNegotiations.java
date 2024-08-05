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
    private final EdcApiHttpClient edcApiHttpClient;

    public ContractNegotiations(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.url = "%s/v3/contractnegotiations".formatted(url);
    }

    public Result<ContractNegotiation> get(String id) {
        var requestBuilder = getRequestBuilder(id);
        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractNegotiation);
    }

    public CompletableFuture<Result<ContractNegotiation>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractNegotiation));
    }

    public Result<String> create(ContractRequest input) {

        var requestBuilder = createRequestBuilder(input);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(ContractRequest input) {

        var requestBuilder = createRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<ContractAgreement> getAgreement(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractAgreement);
    }

    public CompletableFuture<Result<ContractAgreement>> getAgreementAsync(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);
        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractAgreement));
    }

    public Result<String> terminate(TerminateNegotiation input) {

        var requestBuilder = terminateRequestBuilder(input);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateNegotiation input) {

        var requestBuilder = terminateRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    public Result<List<ContractNegotiation>> request(QuerySpec input) {

        var requestBuilder = getContractNegotiationsRequestBuilder(input);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractNegotiations);
    }

    public CompletableFuture<Result<List<ContractNegotiation>>> requestAsync(QuerySpec input) {

        var requestBuilder = getContractNegotiationsRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractNegotiations));
    }

    public Result<String> getState(String id) {
        var requestBuilder = getStateRequestBuilder(id);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getContractNegotiationState);
    }

    public CompletableFuture<Result<String>> getStateAsync(String id) {
        var requestBuilder = getStateRequestBuilder(id);
        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getContractNegotiationState));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder getContractAgreementRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/agreement".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(ContractRequest input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder terminateRequestBuilder(TerminateNegotiation input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/terminate".formatted(this.url, input.id())))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder getContractNegotiationsRequestBuilder(QuerySpec input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder getStateRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/state".formatted(this.url, id)))
                .GET();
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
