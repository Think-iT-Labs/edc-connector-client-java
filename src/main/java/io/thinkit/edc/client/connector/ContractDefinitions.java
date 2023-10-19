package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.TYPE;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ContractDefinitions {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public ContractDefinitions(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Result<ContractDefinition> get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            boolean succeeded = statusCode == 200;
            if (succeeded) {
                var jsonDocument = JsonDocument.of(response.body());
                var jsonArray = JsonLd.expand(jsonDocument).get();
                ContractDefinition contractDefinition = new ContractDefinition(jsonArray.getJsonObject(0));
                return new Result<ContractDefinition>(true, contractDefinition, null);
            } else {
                String error = (statusCode == 400)
                        ? "Request body was malformed"
                        : "A contract definition with the given ID does not exist";
                return new Result<ContractDefinition>(false, error);
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions/%s".formatted(url, id)))
                    .DELETE();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                return new Result<>(true, id, null);
            } else {
                return new Result<>(false, null, "The contract definition cannot be deleted");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public Result<List<ContractDefinition>> request(QuerySpec input) {
        try {
            Map<String, Object> requestBody = Map.of(
                    TYPE,
                    "https://w3id.org/edc/v0.0.1/ns/QuerySpec",
                    "offset",
                    input.offset(),
                    "limit",
                    input.limit(),
                    "sortOrder",
                    input.sortOrder(),
                    "sortField",
                    input.sortField(),
                    "filterExpression",
                    input.filterExpression());

            var jsonRequestBody = new ObjectMapper().writeValueAsString(requestBody);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions/request".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonDocument = JsonDocument.of(response.body());
                var jsonArray = JsonLd.expand(jsonDocument).get();
                List<ContractDefinition> contractDefinitions = jsonArray.stream()
                        .map(s -> new ContractDefinition(s.asJsonObject()))
                        .toList();
                return new Result<>(true, contractDefinitions, null);
            } else {
                return new Result<>(false, "Request body was malformed");
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}