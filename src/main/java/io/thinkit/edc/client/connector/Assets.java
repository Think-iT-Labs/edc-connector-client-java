package io.thinkit.edc.client.connector;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.UnaryOperator;

import static io.thinkit.edc.client.connector.Constants.ID;
import static io.thinkit.edc.client.connector.Constants.TYPE;

public class Assets {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public Assets(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Asset get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var jsonDocument = JsonDocument.of(response.body());
            var jsonArray = JsonLd.expand(jsonDocument).get();

            return new Asset(jsonArray.getJsonObject(0));
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public boolean create(AssetInput input) {
        try {
            Map<String, Object> requestBody = Map.of(
                    ID, input.id(),
                    TYPE, "https://w3id.org/edc/v0.0.1/ns/Asset",
                    "properties", input.properties(),
                    "privateProperties", input.privateProperties(),
                    "dataAddress", input.dataAddress()
            );

            var jsonRequestBody = new ObjectMapper().writeValueAsString(requestBody);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
