package io.thinkit.edc.client.connector;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static io.thinkit.edc.client.connector.Constants.CONTEXT;
import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.ID;
import static io.thinkit.edc.client.connector.Constants.TYPE;
import static io.thinkit.edc.client.connector.Constants.VOCAB;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class Assets {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public Assets(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Result<Asset> get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            boolean succeeded = statusCode == 200;
            if (succeeded) {
                var jsonArray = expand(response.body());
                Asset asset = new Asset(jsonArray.getJsonObject(0));
                return new Result<>(asset, null);
            } else {
                String error = (statusCode == 400)
                        ? "Request body was malformed"
                        : "An asset with the given ID does not exist";
                return new Result<>(error);
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> create(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            boolean succeeded = statusCode == 200;
            if (succeeded) {
                var content = expand(response.body());
                var id = content.getJsonObject(0).getString(ID);
                return new Result<>(id, null);
            } else {
                var error = (statusCode == 400) ? "Request body was malformed" : "Could not create asset";
                return new Result<>(null, error);
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> update(Asset input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets".formatted(url)))
                    .header("content-type", "application/json")
                    .PUT(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var statusCode = response.statusCode();
            if (statusCode == 204) {
                return new Result<>(input.id(), null);
            } else {
                return new Result<>(null, "Asset could not be updated");
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> delete(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/%s".formatted(url, id)))
                    .DELETE();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                return new Result<>(id, null);
            } else {
                return new Result<>(null, "The asset cannot be deleted");
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
                    .POST(ofString(jsonRequestBody));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var assets =
                        jsonArray.stream().map(s -> new Asset(s.asJsonObject())).toList();
                return new Result<>(assets, null);
            } else {
                return new Result<>("Request body was malformed");
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    private JsonArray expand(InputStream body) throws JsonLdError {
        var jsonDocument = JsonDocument.of(body);
        return JsonLd.expand(jsonDocument).get();
    }

    private JsonObject compact(Asset input) throws JsonLdError {
        var expanded = JsonDocument.of(input.raw());
        var context = JsonDocument.of(Json.createObjectBuilder()
                .add(CONTEXT, Json.createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .build());
        return JsonLd.compact(expanded, context).get();
    }
}
