package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.ContractDefinition;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ContractDefinitions extends ManagementResource {
    private final String url;

    public ContractDefinitions(EdcClientContext context) {
        super(context);
        url = "%s/v3/contractdefinitions".formatted(managementUrl);
    }

    public Result<ContractDefinition> get(String id) {
        var requestBuilder = getRequestBuilder(id);
        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getContractDefinition);
    }

    public CompletableFuture<Result<ContractDefinition>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractDefinition));
    }

    public Result<String> create(ContractDefinition input) {

        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(content -> content.getJsonObject(0)
                .getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(ContractDefinition input) {

        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<String> delete(String id) {
        var requestBuilder = deleteRequestBuilder(id);
        return context.httpClient().send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = deleteRequestBuilder(id);
        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    public Result<List<ContractDefinition>> request(QuerySpec input) {

        var requestBuilder = getContractDefinitionsRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getContractDefinitions);
    }

    public CompletableFuture<Result<List<ContractDefinition>>> requestAsync(QuerySpec input) {

        var requestBuilder = getContractDefinitionsRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractDefinitions));
    }

    public Result<String> update(ContractDefinition input) {

        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> updateAsync(ContractDefinition input) {

        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(ContractDefinition input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder updateRequestBuilder(ContractDefinition input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder deleteRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .DELETE();
    }

    private HttpRequest.Builder getContractDefinitionsRequestBuilder(QuerySpec input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
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
