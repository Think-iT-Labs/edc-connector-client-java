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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ContractDefinitions {
    private final String url;
    private final ManagementApiHttpClient managementApiHttpClient;

    public ContractDefinitions(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(httpClient, interceptor);
        this.url = url;
    }

    public Result<ContractDefinition> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractDefinition);
    }

    public CompletableFuture<Result<ContractDefinition>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(this.url, id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractDefinition));
    }

    public Result<String> create(ContractDefinition input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(ContractDefinition input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<String> delete(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(this.url, id)))
                .DELETE();
        return this.managementApiHttpClient.send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(this.url, id)))
                .DELETE();
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    public Result<List<ContractDefinition>> request(QuerySpec input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getContractDefinitions);
    }

    public CompletableFuture<Result<List<ContractDefinition>>> requestAsync(QuerySpec input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
        return this.managementApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractDefinitions));
    }

    public Result<String> update(ContractDefinition input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions".formatted(this.url)))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
        return this.managementApiHttpClient.send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> updateAsync(ContractDefinition input) {

        var requestBody = compact(input);

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions".formatted(this.url)))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
        return this.managementApiHttpClient
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> input.id()));
    }

    private ContractDefinition getContractDefinition(JsonArray array) {
        return ContractDefinition.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private List<ContractDefinition> getContractDefinitions(JsonArray array) {
        return array.stream()
                .map(s -> ContractDefinition.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
