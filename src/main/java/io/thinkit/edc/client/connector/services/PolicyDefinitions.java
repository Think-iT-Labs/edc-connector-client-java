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

public class PolicyDefinitions {

    private final String url;
    private final EdcApiHttpClient edcApiHttpClient;

    public PolicyDefinitions(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.url = "%s/v3/policydefinitions".formatted(url);
    }

    public Result<PolicyDefinition> get(String id) {
        var requestBuilder = getRequestBuilder(id);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getPolicyDefinition);
    }

    public CompletableFuture<Result<PolicyDefinition>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);
        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getPolicyDefinition));
    }

    public Result<String> create(PolicyDefinition input) {

        var requestBuilder = createRequestBuilder(input);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(PolicyDefinition input) {

        var requestBuilder = createRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<String> update(PolicyDefinition input) {

        var requestBuilder = updateRequestBuilder(input);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> updateAsync(PolicyDefinition input) {

        var requestBuilder = updateRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> delete(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return this.edcApiHttpClient.send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    public Result<List<PolicyDefinition>> request(QuerySpec input) {

        var requestBuilder = getPolicyDefinitionsRequestBuilder(input);

        return this.edcApiHttpClient
                .send(requestBuilder)
                .map(JsonLdUtil::expand)
                .map(this::getPolicyDefinitions);
    }

    public CompletableFuture<Result<List<PolicyDefinition>>> requestAsync(QuerySpec input) {

        var requestBuilder = getPolicyDefinitionsRequestBuilder(input);

        return this.edcApiHttpClient.sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getPolicyDefinitions));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(PolicyDefinition input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder updateRequestBuilder(PolicyDefinition input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, input.id())))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder deleteRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .DELETE();
    }

    private HttpRequest.Builder getPolicyDefinitionsRequestBuilder(QuerySpec input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private PolicyDefinition getPolicyDefinition(JsonArray array) {
        return PolicyDefinition.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private List<PolicyDefinition> getPolicyDefinitions(JsonArray array) {
        return array.stream()
                .map(s -> PolicyDefinition.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
