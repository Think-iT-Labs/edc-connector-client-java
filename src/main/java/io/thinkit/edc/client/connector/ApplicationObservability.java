package io.thinkit.edc.client.connector;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.UnaryOperator;

public class ApplicationObservability {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public ApplicationObservability(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Result<HealthStatus> checkHealth() {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/check/health".formatted(url)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonDocument = JsonDocument.of(response.body());
                var content = jsonDocument.getJsonContent().get();
                var healthStatus = new HealthStatus(content.asJsonObject());
                return new Result<>(healthStatus, null);
            } else {
                return new Result<>("Request body was malformed");
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<HealthStatus> checkReadiness() {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/check/readiness".formatted(url)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonDocument = JsonDocument.of(response.body());
                var content = jsonDocument.getJsonContent().get();
                var healthStatus = new HealthStatus(content.asJsonObject());
                return new Result<>(healthStatus, null);
            } else {
                return new Result<>("Request body was malformed");
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
