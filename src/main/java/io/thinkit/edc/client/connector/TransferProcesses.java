package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.JsonLdUtil.expand;

import com.apicatalog.jsonld.JsonLdError;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.UnaryOperator;

public class TransferProcesses {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public TransferProcesses(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Result<TransferProcess> get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var transferProcess = TransferProcess.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(transferProcess, null);
            } else {
                var error = (statusCode == 400)
                        ? "Request body was malformed"
                        : "A transfer process with the given ID does not exist";
                return new Result<>(error);
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<TransferState> getState(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/transferprocesses/%s/state".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var state = TransferState.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(state, null);
            } else {
                var error = (statusCode == 400)
                        ? "Request body was malformed"
                        : "An transfer process with the given ID does not exist";
                return new Result<>(error);
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
