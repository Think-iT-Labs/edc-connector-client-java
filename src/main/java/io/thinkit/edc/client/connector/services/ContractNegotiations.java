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
import java.util.function.UnaryOperator;

public class ContractNegotiations {
    private final ManagementApiHttpClient managementApiHttpClient;

    public ContractNegotiations(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(url, httpClient, interceptor);
    }

    public Result<ContractNegotiation> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "get", this::getContractNegotiation);
    }

    public CompletableFuture<Result<ContractNegotiation>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getContractNegotiation);
    }

    public Result<String> create(ContractRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "");

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(ContractRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.sendAsync(requestBuilder, "");

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<ContractAgreement> getAgreement(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/v2/contractnegotiations/%s/agreement".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "get", this::getContractAgreement);
    }

    public CompletableFuture<Result<ContractAgreement>> getAgreementAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(
                        "%s/v2/contractnegotiations/%s/agreement".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getContractAgreement);
    }

    public Result<String> terminate(TerminateNegotiation input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/%s/terminate"
                            .formatted(managementApiHttpClient.getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateNegotiation input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/%s/terminate"
                            .formatted(managementApiHttpClient.getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.sendAsync(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<List<ContractNegotiation>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "get", this::getContractNegotiations);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<ContractNegotiation>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getContractNegotiations);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> getState(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/state".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "getStatus", this::getContractNegotiationState);
    }

    public CompletableFuture<Result<String>> getStateAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractnegotiations/%s/state".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "getStatus", this::getContractNegotiationState);
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
