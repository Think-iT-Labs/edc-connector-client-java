package io.thinkit.edc.client.connector;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.UnaryOperator;

public class PolicyDefinitions {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public PolicyDefinitions(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Result<PolicyDefinition> get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/policydefinitions/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            boolean succeeded = statusCode == 200;
            if (succeeded) {
                var jsonDocument = JsonDocument.of(response.body());
                var jsonArray = JsonLd.expand(jsonDocument).get();
                PolicyDefinition policyDefinition = new PolicyDefinition(jsonArray.getJsonObject(0));
                return new Result<>(policyDefinition, null);
            } else {
                String error = (statusCode == 400)
                        ? "Request body was malformed"
                        : "A policy definition with the given ID does not exist";
                return new Result<>(error);
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
