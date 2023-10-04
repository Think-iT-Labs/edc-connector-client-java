package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.ID;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

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

    public Result<String> create(AssetInput input) {
        try {
            Map<String, Object> requestBody = Map.of(
                    ID,
                    input.id(),
                    TYPE,
                    "https://w3id.org/edc/v0.0.1/ns/Asset",
                    "properties",
                    input.properties(),
                    "privateProperties",
                    input.privateProperties(),
                    "dataAddress",
                    input.dataAddress());

            var jsonRequestBody = new ObjectMapper().writeValueAsString(requestBody);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            boolean succeeded = statusCode == 200;
            if (succeeded) {
                var jsonDocument = JsonDocument.of(response.body());
                var content = jsonDocument.getJsonContent().get();
                String id = content.asJsonObject().getString("@id");
                return new Result(true, id, null);
            } else {
                String error = (statusCode == 400) ? "Request body was malformed" : "Could not create asset";
                return new Result(false, null, error);
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> update(AssetInput input) {
        try {
            Map<String, Object> requestBody = Map.of(
                    ID,
                    input.id(),
                    TYPE,
                    "https://w3id.org/edc/v0.0.1/ns/Asset",
                    "properties",
                    input.properties(),
                    "privateProperties",
                    input.privateProperties(),
                    "dataAddress",
                    input.dataAddress());

            var jsonRequestBody = new ObjectMapper().writeValueAsString(requestBody);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(url)))
                    .header("content-type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonRequestBody));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                return new Result(true, input.id(), null);
            } else {
                return new Result(false, null, "Asset could not be updated");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Result delete(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/%s".formatted(url, id)))
                    .DELETE();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                return new Result(true, id, null);
            } else {
                return new Result(false, null, "The asset cannot be deleted");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Result<List<Asset>> request(QuerySpec input) {
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
                    .uri(URI.create("%s/v3/assets/request".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonDocument = JsonDocument.of(response.body());
                var jsonArray = JsonLd.expand(jsonDocument).get();
                List<Asset> assets = new ArrayList<>();
                assets =
                        jsonArray.stream().map(s -> new Asset(s.asJsonObject())).collect(Collectors.toList());
                return new Result<List<Asset>>(true, assets, null);
            } else {
                return new Result<List<Asset>>(false, "Request body was malformed");
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
