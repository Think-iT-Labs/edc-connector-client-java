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

public class PolicyDefinitions {

    private final ManagementApiHttpClient managementApiHttpClient;

    public PolicyDefinitions(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        managementApiHttpClient = new ManagementApiHttpClient(url, httpClient, interceptor);
    }

    public Result<PolicyDefinition> get(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/policydefinitions/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.send(requestBuilder, "get", this::getPolicyDefinition);
    }

    public CompletableFuture<Result<PolicyDefinition>> getAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/policydefinitions/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .GET();
        return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getPolicyDefinition);
    }

    public Result<String> create(PolicyDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));
            return this.managementApiHttpClient.send(requestBuilder, "");
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> createAsync(PolicyDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, "");

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> update(PolicyDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "%s/v2/policydefinitions/%s".formatted(managementApiHttpClient.getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            return this.managementApiHttpClient.send(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<String>> updateAsync(PolicyDefinition input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "%s/v2/policydefinitions/%s".formatted(managementApiHttpClient.getUrl(), input.id())))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, input.id());

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/policydefinitions/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .DELETE();

        return this.managementApiHttpClient.send(requestBuilder, id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v2/policydefinitions/%s".formatted(managementApiHttpClient.getUrl(), id)))
                .DELETE();
        return this.managementApiHttpClient.sendAsync(requestBuilder, id);
    }

    public Result<List<PolicyDefinition>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.send(requestBuilder, "get", this::getPolicyDefinitions);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Result<List<PolicyDefinition>>> requestAsync(QuerySpec input) {
        try {
            var requestBody = compact(input);
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/request".formatted(managementApiHttpClient.getUrl())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            return this.managementApiHttpClient.sendAsync(requestBuilder, "get", this::getPolicyDefinitions);

        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
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
