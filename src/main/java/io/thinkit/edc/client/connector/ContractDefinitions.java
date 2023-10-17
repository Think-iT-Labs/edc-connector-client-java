package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.ID;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
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

    public Result<String> create(ContractDefinitionInput input) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put(ID, input.id());
            requestBody.put("accessPolicyId", input.accessPolicyId());
            requestBody.put("contractPolicyId", input.contractPolicyId());
            requestBody.put("assetsSelector", input.assetsSelector());

            var jsonRequestBody = new ObjectMapper().writeValueAsString(requestBody);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractdefinitions".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            boolean succeeded = statusCode == 200;
            if (succeeded) {
                var jsonDocument = JsonDocument.of(response.body());
                var jsonArray = JsonLd.expand(jsonDocument).get();
                String id = jsonArray.getJsonObject(0).getString(ID);
                return new Result<String>(true, id, null);
            } else {
                String error =
                        (statusCode == 400) ? "Request body was malformed" : "Could not create contract definition";
                return new Result<String>(false, null, error);
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
