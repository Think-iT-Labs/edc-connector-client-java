package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import io.thinkit.edc.client.connector.model.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.UnaryOperator;

public class ContractNegotiations {
    private final String url;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequest.Builder> interceptor;

    public ContractNegotiations(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        this.url = url;
        this.httpClient = httpClient;
        this.interceptor = interceptor;
    }

    public Result<ContractNegotiation> get(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/%s".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var contractNegotiation = ContractNegotiation.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(contractNegotiation, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> create(ContractRequest input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations".formatted(url)))
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
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<ContractAgreement> getAgreement(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/%s/agreement".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var contractAgreement = ContractAgreement.Builder.newInstance()
                        .raw(jsonArray.getJsonObject(0))
                        .build();
                return new Result<>(contractAgreement, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> terminate(TerminateNegotiation input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/%s/terminate".formatted(url, input.id())))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                return new Result<>(input.id(), null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<List<ContractNegotiation>> request(QuerySpec input) {
        try {
            var requestBody = compact(input);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/request".formatted(url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody.toString()));

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonArray = expand(response.body());
                var contractNegotiations = jsonArray.stream()
                        .map(s -> ContractNegotiation.Builder.newInstance()
                                .raw(s.asJsonObject())
                                .build())
                        .toList();
                return new Result<>(contractNegotiations, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }

        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public Result<String> getState(String id) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("%s/v2/contractnegotiations/%s/state".formatted(url, id)))
                    .GET();

            var request = interceptor.apply(requestBuilder).build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var statusCode = response.statusCode();
            if (statusCode == 200) {
                var jsonDocument = JsonDocument.of(response.body());
                var content = jsonDocument.getJsonContent().get();
                var state = content.asJsonObject().getString("state");
                return new Result<>(state, null);
            } else {
                var error = deserializeToArray(response.body()).stream()
                        .map(s -> new ApiErrorDetail(s.asJsonObject()))
                        .toList();
                return new Result<>(error);
            }
        } catch (IOException | InterruptedException | JsonLdError e) {
            throw new RuntimeException(e);
        }
    }
}
