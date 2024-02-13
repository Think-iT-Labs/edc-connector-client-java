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

public class ContractAgreements extends Service {

    public ContractAgreements(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        super(url, httpClient, interceptor);
    }

    public Result<ContractAgreement> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractagreements/%s".formatted(getUrl(), id)))
                .GET();
        return this.send(
                requestBuilder,
                (Function<JsonObject, ContractAgreement>) this::getContractAgreement,
                this::getResponse);
    }

    public CompletableFuture<Result<ContractAgreement>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractagreements/%s".formatted(getUrl(), id)))
                .GET();
        return this.sendAsync(
                requestBuilder,
                (Function<JsonObject, ContractAgreement>) this::getContractAgreement,
                this::getResponse);
    }

    public Result<ContractNegotiation> getNegotiation(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractagreements/%s/negotiation".formatted(getUrl(), id)))
                .GET();
        return this.send(
                requestBuilder,
                (Function<JsonObject, ContractNegotiation>) this::getContractNegotiation,
                this::getResponse);
    }

    public CompletableFuture<Result<ContractNegotiation>> getNegotiationAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractagreements/%s/negotiation".formatted(getUrl(), id)))
                .GET();
        return this.sendAsync(
                requestBuilder,
                (Function<JsonObject, ContractNegotiation>) this::getContractNegotiation,
                this::getResponse);
    }

    public Result<List<ContractAgreement>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractagreements/request".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(
                    requestBuilder,
                    (Function<JsonArray, List<ContractAgreement>>) this::getContractAgreements,
                    this::requestResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<ContractAgreement>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractagreements/request".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.sendAsync(
                    requestBuilder,
                    (Function<JsonArray, List<ContractAgreement>>) this::getContractAgreements,
                    this::requestResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private ContractAgreement getContractAgreement(JsonObject object) {
        return ContractAgreement.Builder.newInstance().raw(object).build();
    }

    private ContractNegotiation getContractNegotiation(JsonObject object) {
        return ContractNegotiation.Builder.newInstance().raw(object).build();
    }

    private List<ContractAgreement> getContractAgreements(JsonArray array) {
        return array.stream()
                .map(s -> ContractAgreement.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
