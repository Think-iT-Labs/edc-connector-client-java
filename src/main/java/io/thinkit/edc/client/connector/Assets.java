package io.thinkit.edc.client.connector;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Assets {
    private final String url;

    public Assets(String url) {
        this.url = url;
    }

    public Object get(String id) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("%s/v3/assets/%s".formatted(url, id)))
                .GET().build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
