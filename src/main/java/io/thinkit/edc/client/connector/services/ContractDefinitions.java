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

public class ContractDefinitions extends Service {

    public ContractDefinitions(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        super(url, httpClient, interceptor);
    }

    public Result<ContractDefinition> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(getUrl(), id)))
                .GET();
        return this.send(
                requestBuilder,
                (Function<JsonObject, ContractDefinition>) this::getContractDefinition,
                this::getResponse);
    }

    public CompletableFuture<Result<ContractDefinition>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(getUrl(), id)))
                .GET();
        return this.sendAsync(
                requestBuilder,
                (Function<JsonObject, ContractDefinition>) this::getContractDefinition,
                this::getResponse);
    }

    public Result<String> create(ContractDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(requestBuilder, this::createResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(ContractDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.sendAsync(requestBuilder, this::createResponse);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(getUrl(), id)))
                .DELETE();
        return this.send(requestBuilder, id, this::deleteAndUpdateResponse);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(getUrl(), id)))
                .DELETE();
        return this.sendAsync(requestBuilder, id, this::deleteAndUpdateResponse);
    }

    public Result<List<ContractDefinition>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions/request".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.send(
                    requestBuilder,
                    (Function<JsonArray, List<ContractDefinition>>) this::getContractDefinitions,
                    this::requestResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<ContractDefinition>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions/request".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.sendAsync(
                    requestBuilder,
                    (Function<JsonArray, List<ContractDefinition>>) this::getContractDefinitions,
                    this::requestResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> update(ContractDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));
            return this.send(requestBuilder, input.id(), this::deleteAndUpdateResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> updateAsync(ContractDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(getUrl())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));
            return this.sendAsync(requestBuilder, input.id(), this::deleteAndUpdateResponse);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private ContractDefinition getContractDefinition(JsonObject object) {
        return ContractDefinition.Builder.newInstance().raw(object).build();
    }

    private List<ContractDefinition> getContractDefinitions(JsonArray array) {
        return array.stream()
                .map(s -> ContractDefinition.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .toList();
    }
}
