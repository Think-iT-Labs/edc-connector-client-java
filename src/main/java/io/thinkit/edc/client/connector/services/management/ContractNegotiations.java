package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdContractNegotiation;
import io.thinkit.edc.client.connector.model.pojo.PojoContractNegotiation;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ContractNegotiations extends ManagementResource {
    private final String url;

    public ContractNegotiations(EdcClientContext context) {
        super(context);
        url = "%s/contractnegotiations".formatted(managementUrl);
    }

    public Result<ContractNegotiation> get(String id) {
        var requestBuilder = getRequestBuilder(id);
        Function<InputStream, Result<ContractNegotiation>> function = managementVersion.equals(V3)
                ? stream -> Result.succeded(stream).map(JsonLdUtil::expand).map(this::getContractNegotiation)
                : stream -> Result.succeded(stream).map(deserializeContractNegotiation());

        return context.httpClient().send(requestBuilder).compose(function);
    }

    public CompletableFuture<Result<ContractNegotiation>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);
        Function<Result<InputStream>, Result<ContractNegotiation>> function = managementVersion.equals(V3)
                ? result -> result.map(JsonLdUtil::expand).map(this::getContractNegotiation)
                : result -> result.map(deserializeContractNegotiation());

        return context.httpClient().sendAsync(requestBuilder).thenApply(function);
    }

    public Result<String> create(ContractRequest input) {

        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(content -> content.getJsonObject(0)
                .getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(ContractRequest input) {

        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<ContractAgreement> getAgreement(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(this::getContractAgreement);
    }

    public CompletableFuture<Result<ContractAgreement>> getAgreementAsync(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);
        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(this::getContractAgreement));
    }

    public Result<String> terminate(TerminateNegotiation input) {

        var requestBuilder = terminateRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> terminateAsync(TerminateNegotiation input) {

        var requestBuilder = terminateRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    public Result<List<ContractNegotiation>> request(QuerySpec input) {

        var requestBuilder = getContractNegotiationsRequestBuilder(input);
        Function<Result<InputStream>, Result<List<ContractNegotiation>>> function = managementVersion.equals(V3)
                ? result -> result.map(JsonLdUtil::expand).map(this::getContractNegotiations)
                : result -> result.map(deserializeContractNegotiations());

        return function.apply(context.httpClient().send(requestBuilder));
    }

    public CompletableFuture<Result<List<ContractNegotiation>>> requestAsync(QuerySpec input) {

        var requestBuilder = getContractNegotiationsRequestBuilder(input);
        Function<Result<InputStream>, Result<List<ContractNegotiation>>> function = managementVersion.equals(V3)
                ? result -> result.map(JsonLdUtil::expand).map(this::getContractNegotiations)
                : result -> result.map(deserializeContractNegotiations());

        return context.httpClient().sendAsync(requestBuilder).thenApply(function);
    }

    public Result<String> getState(String id) {
        var requestBuilder = getStateRequestBuilder(id);

        return context.httpClient()
                .send(requestBuilder)
                .map(JsonLdUtil::ToJsonObject)
                .map(this::getContractNegotiationState);
    }

    public CompletableFuture<Result<String>> getStateAsync(String id) {
        var requestBuilder = getStateRequestBuilder(id);
        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::ToJsonObject)
                .map(this::getContractNegotiationState));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder getContractAgreementRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/agreement".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(ContractRequest input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder terminateRequestBuilder(TerminateNegotiation input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/terminate".formatted(this.url, input.id())))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder getContractNegotiationsRequestBuilder(QuerySpec input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder getStateRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/state".formatted(this.url, id)))
                .GET();
    }

    private ContractNegotiation getContractNegotiation(JsonArray array) {
        return JsonLdContractNegotiation.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private ContractAgreement getContractAgreement(JsonArray array) {
        return ContractAgreement.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private String getContractNegotiationState(JsonObject content) {
        return content.asJsonObject().getString("state");
    }

    private List<ContractNegotiation> getContractNegotiations(JsonArray array) {
        return array.stream()
                .map(s -> JsonLdContractNegotiation.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .map(ContractNegotiation.class::cast)
                .toList();
    }

    private Function<InputStream, List<ContractNegotiation>> deserializeContractNegotiations() {
        return stream -> {
            try {
                return context
                        .objectMapper()
                        .readValue(stream, new TypeReference<List<PojoContractNegotiation>>() {})
                        .stream()
                        .map(ContractNegotiation.class::cast)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<InputStream, ContractNegotiation> deserializeContractNegotiation() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, PojoContractNegotiation.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
