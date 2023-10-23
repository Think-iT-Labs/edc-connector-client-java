package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.ID;
import static io.thinkit.edc.client.connector.JsonLdUtil.compact;
import static io.thinkit.edc.client.connector.JsonLdUtil.expand;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.UnaryOperator;

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
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var asset = Asset.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(asset, null);
            } else {
                var error = (statusCode == 400)
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
            if (statusCode == 200) {
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
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v3/assets/request".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var assets = jsonArray.stream()
                        .map(s -> Asset.Builder.newInstance()
                                .raw(s.asJsonObject())
                                .build())
                        .toList();
                return new Result<>(assets, null);
            } else {
                return new Result<>("Request body was malformed");
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
