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
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(url, id)))
                .GET();

        var request = interceptor.apply(requestBuilder).build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var jsonDocument = JsonDocument.of(response.body());
            var jsonArray = JsonLd.expand(jsonDocument).get();

            return new Asset(jsonArray.getJsonObject(0));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
