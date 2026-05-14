package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.PolicyDefinition;
import io.thinkit.edc.client.connector.model.PolicyValidationResult;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdPolicyDefinition;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdPolicyValidationResult;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
import io.thinkit.edc.client.connector.model.pojo.PojoPolicyDefinition;
import io.thinkit.edc.client.connector.model.pojo.PojoPolicyValidationResult;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class PolicyDefinitions extends ManagementResource {

    private final String url;

    public PolicyDefinitions(EdcClientContext context) {
        super(context);
        url = "%s/policydefinitions".formatted(managementUrl);
    }

    public Result<PolicyDefinition> get(String id) {
        var requestBuilder = getRequestBuilder(id);
        var deserialize = responseDeserializer(this::getPolicyDefinition, deserializePolicyDefinition());
        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<PolicyDefinition>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);
        var deserialize = responseDeserializer(this::getPolicyDefinition, deserializePolicyDefinition());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    public Result<String> create(PolicyDefinition input) {

        var requestBuilder = createRequestBuilder(input);

        return context.httpClient()
                .send(requestBuilder)
                .map(body -> Json.createReader(body).readObject().getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(PolicyDefinition input) {

        var requestBuilder = createRequestBuilder(input);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result ->
                        result.map(body -> Json.createReader(body).readObject().getString(ID)));
    }

    public Result<String> update(PolicyDefinition input) {

        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(result -> ((JsonLdPolicyDefinition) input).id());
    }

    public CompletableFuture<Result<String>> updateAsync(PolicyDefinition input) {

        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> ((JsonLdPolicyDefinition) input).id()));
    }

    public Result<String> delete(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return context.httpClient().send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    public Result<List<PolicyDefinition>> request(QuerySpec input) {

        var requestBuilder = getPolicyDefinitionsRequestBuilder(input);
        var deserialize = responseDeserializer(this::getPolicyDefinitions, deserializePolicyDefinitions());

        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<List<PolicyDefinition>>> requestAsync(QuerySpec input) {

        var requestBuilder = getPolicyDefinitionsRequestBuilder(input);
        var deserialize = responseDeserializer(this::getPolicyDefinitions, deserializePolicyDefinitions());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    public Result<PolicyValidationResult> validate(String id) {
        var requestBuilder = validateRequestBuilder(id);
        var deserialize = responseDeserializer(this::getPolicyValidationResult, deserializePolicyValidationResult());
        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<PolicyValidationResult>> validateAsync(String id) {
        var requestBuilder = validateRequestBuilder(id);
        var deserialize = responseDeserializer(this::getPolicyValidationResult, deserializePolicyValidationResult());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(PolicyDefinition input) {
        var requestBody = compact((JsonLdPolicyDefinition) input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder updateRequestBuilder(PolicyDefinition input) {
        var requestBody = compact((JsonLdPolicyDefinition) input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, ((JsonLdPolicyDefinition) input).id())))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder deleteRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .DELETE();
    }

    private HttpRequest.Builder getPolicyDefinitionsRequestBuilder(QuerySpec input) {
        var requestBody = compact((JsonLdQuerySpec) input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder validateRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/validate".formatted(this.url, id)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody());
    }

    private PolicyDefinition getPolicyDefinition(JsonArray array) {
        return JsonLdPolicyDefinition.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private PolicyValidationResult getPolicyValidationResult(JsonArray array) {
        return JsonLdPolicyValidationResult.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private List<PolicyDefinition> getPolicyDefinitions(JsonArray array) {
        return array.stream()
                .map(s -> JsonLdPolicyDefinition.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .map(PolicyDefinition.class::cast)
                .toList();
    }

    private Function<InputStream, List<PolicyDefinition>> deserializePolicyDefinitions() {
        return stream -> {
            try {
                return context
                        .objectMapper()
                        .readValue(stream, new TypeReference<List<PojoPolicyDefinition>>() {})
                        .stream()
                        .map(PolicyDefinition.class::cast)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<InputStream, PolicyDefinition> deserializePolicyDefinition() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, PojoPolicyDefinition.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<InputStream, PolicyValidationResult> deserializePolicyValidationResult() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, PojoPolicyValidationResult.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
