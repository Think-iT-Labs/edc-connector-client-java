package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ContractAgreements {
    private final String url;
    private final EdcApiHttpClient edcApiHttpClient;

    public ContractAgreements(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.url = "%s/v3/contractagreements".formatted(url);
    }

    public Result<ContractAgreement> get(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractAgreement);
    }

    public CompletableFuture<Result<ContractAgreement>> getAsync(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);
        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractAgreement));
    }

    public Result<ContractNegotiation> getNegotiation(String id) {
        var requestBuilder = getContractNegotiationRequestBuilder(id);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractNegotiation);
    }

    public CompletableFuture<Result<ContractNegotiation>> getNegotiationAsync(String id) {
        var requestBuilder = getContractNegotiationRequestBuilder(id);
        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractNegotiation));
    }

    public Result<List<ContractAgreement>> request(QuerySpec input) {
        var requestBuilder = ContractAgreementsRequestBuilder(input);
        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractAgreements);
    }

    public CompletableFuture<Result<List<ContractAgreement>>> requestAsync(QuerySpec input) {
        var requestBuilder = ContractAgreementsRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractAgreements));
    }

    private HttpRequest.Builder getContractAgreementRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder getContractNegotiationRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/negotiation".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder ContractAgreementsRequestBuilder(QuerySpec input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private ContractAgreement getContractAgreement(JsonArray array) {
        return ContractAgreement.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private ContractNegotiation getContractNegotiation(JsonArray array) {
        return ContractNegotiation.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private List<ContractAgreement> getContractAgreements(JsonArray array) {
        return array.stream()
                .map(s -> ContractAgreement.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
