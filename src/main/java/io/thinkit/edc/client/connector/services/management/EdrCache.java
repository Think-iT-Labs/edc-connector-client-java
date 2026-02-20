package io.thinkit.edc.client.connector.services.management;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdDataAddress;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdEdr;
import io.thinkit.edc.client.connector.model.pojo.PojoDataAddress;
import io.thinkit.edc.client.connector.model.pojo.PojoEdr;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import jakarta.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class EdrCache extends ManagementResource {
    private final String url;

    public EdrCache(EdcClientContext context) {
        super(context);
        url = "%s/edrs".formatted(managementUrl);
    }

    public Result<String> delete(String transferProcessId) {
        var requestBuilder = getDeleteRequestBuilder(transferProcessId);

        return context.httpClient().send(requestBuilder).map(result -> transferProcessId);
    }

    public CompletableFuture<Result<String>> deleteAsync(String transferProcessId) {
        var requestBuilder = getDeleteRequestBuilder(transferProcessId);

        return context.httpClient()
                .sendAsync(requestBuilder)
                .thenApply(result -> result.map(content -> transferProcessId));
    }

    public Result<DataAddress> dataAddress(String transferProcessId) {
        var requestBuilder = getDataAddressRequestBuilder(transferProcessId);
        var deserialize = responseDeserializer(this::getDataAddress, deserializeDataAddress());

        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<DataAddress>> dataAddressAsync(String transferProcessId) {
        var requestBuilder = getDataAddressRequestBuilder(transferProcessId);
        var deserialize = responseDeserializer(this::getDataAddress, deserializeDataAddress());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    public Result<Edr> request(QuerySpec input) {
        var requestBuilder = getRequestBuilder(input);
        var deserialize = responseDeserializer(this::getEDR, deserializeEdr());

        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<Edr>> requestAsync(QuerySpec input) {
        var requestBuilder = getRequestBuilder(input);
        var deserialize = responseDeserializer(this::getEDR, deserializeEdr());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    private HttpRequest.Builder getDeleteRequestBuilder(String transferProcessId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, transferProcessId)))
                .DELETE();
    }

    private HttpRequest.Builder getDataAddressRequestBuilder(String transferProcessId) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/dataaddress".formatted(this.url, transferProcessId)))
                .GET();
    }

    private HttpRequest.Builder getRequestBuilder(QuerySpec input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private DataAddress getDataAddress(JsonArray array) {
        return JsonLdDataAddress.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private Edr getEDR(JsonArray array) {
        return JsonLdEdr.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Function<InputStream, DataAddress> deserializeDataAddress() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, PojoDataAddress.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<InputStream, Edr> deserializeEdr() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, new TypeReference<List<PojoEdr>>() {}).stream()
                        .map(Edr.class::cast)
                        .toList()
                        .get(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
