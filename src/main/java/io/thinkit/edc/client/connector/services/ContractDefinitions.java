package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import io.thinkit.edc.client.connector.model.*;
import jakarta.json.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ContractDefinitions {
    private final ManagementApiHttpClient managementApiHttpClient;

    public ContractDefinitions(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(url, httpClient, interceptor);
    }

    public Result<ContractDefinition> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "get", this::getContractDefinition);
    }

    public CompletableFuture<Result<ContractDefinition>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getContractDefinition);
    }

    public Result<String> create(ContractDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "");

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(ContractDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.sendAsync(requestBuilder, "");
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .DELETE();
        return this.managementApiHttpClient.send(requestBuilder, id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .DELETE();
        return this.managementApiHttpClient.sendAsync(requestBuilder, id);
    }

    public Result<List<ContractDefinition>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "get", this::getContractDefinitions);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<ContractDefinition>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getContractDefinitions);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> update(ContractDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> updateAsync(ContractDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));
            return this.managementApiHttpClient.sendAsync(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
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
