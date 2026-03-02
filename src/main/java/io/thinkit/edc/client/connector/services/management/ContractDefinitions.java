package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.ContractDefinition;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdContractDefinition;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
import io.thinkit.edc.client.connector.model.pojo.PojoContractDefinition;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ContractDefinitions extends ManagementResource {
    private final String url;

    public ContractDefinitions(EdcClientContext context) {
        super(context);
        url = "%s/contractdefinitions".formatted(managementUrl);
    }

    public Result<ContractDefinition> get(String id) {
        var requestBuilder = getRequestBuilder(id);
        var deserialize = responseDeserializer(this::getContractDefinition, deserializeContractDefinition());
        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<ContractDefinition>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);
        var deserialize = responseDeserializer(this::getContractDefinition, deserializeContractDefinition());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
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
        var deserialize = responseDeserializer(this::getContractDefinitions, deserializeContractDefinitions());

        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<List<ContractDefinition>>> requestAsync(QuerySpec input) {

        var requestBuilder = getContractDefinitionsRequestBuilder(input);
        var deserialize = responseDeserializer(this::getContractDefinitions, deserializeContractDefinitions());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    public Result<String> update(ContractDefinition input) {

        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(result -> ((JsonLdContractDefinition) input).id());
    }

    public CompletableFuture<Result<String>> updateAsync(ContractDefinition input) {

        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> ((JsonLdContractDefinition) input).id()));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(ContractDefinition input) {
        var requestBody = compact((JsonLdContractDefinition) input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder updateRequestBuilder(ContractDefinition input) {
        var requestBody = compact((JsonLdContractDefinition) input);
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
        var requestBody = compact((JsonLdQuerySpec) input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private ContractDefinition getContractDefinition(JsonArray array) {
        return JsonLdContractDefinition.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private List<ContractDefinition> getContractDefinitions(JsonArray array) {
        return array.stream()
                .map(s -> JsonLdContractDefinition.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .map(ContractDefinition.class::cast)
                .toList();
    }

    private Function<InputStream, List<ContractDefinition>> deserializeContractDefinitions() {
        return stream -> {
            try {
                return context
                        .objectMapper()
                        .readValue(stream, new TypeReference<List<PojoContractDefinition>>() {})
                        .stream()
                        .map(ContractDefinition.class::cast)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<InputStream, ContractDefinition> deserializeContractDefinition() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, PojoContractDefinition.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
