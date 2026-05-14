package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.*;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.model.jsonld.*;
import io.thinkit.edc.client.connector.model.pojo.PojoCatalog;
import io.thinkit.edc.client.connector.model.pojo.PojoDataset;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import jakarta.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Catalogs extends ManagementResource {
    private final String url;

    public Catalogs(EdcClientContext context) {
        super(context);
        url = "%s/catalog".formatted(managementUrl);
    }

    public Result<Catalog> request(CatalogRequest input) {
        var requestBuilder = getCatalogRequestBuilder(input);
        var deserialize = responseDeserializer(this::getCatalog, deserializeCatalog());

        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<Catalog>> requestAsync(CatalogRequest input) {
        var requestBuilder = getCatalogRequestBuilder(input);
        var deserialize = responseDeserializer(this::getCatalog, deserializeCatalog());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    public Result<Dataset> requestDataset(CatalogRequest input) {

        var requestBuilder = requestDatasetRequestBuilder(input);

        var deserialize = responseDeserializer(this::getDataset, deserializeDataset());

        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<Dataset>> requestDatasetAsync(CatalogRequest input) {

        var requestBuilder = requestDatasetRequestBuilder(input);
        var deserialize = responseDeserializer(this::getDataset, deserializeDataset());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    private HttpRequest.Builder requestDatasetRequestBuilder(CatalogRequest input) {
        String requestBody = null;

        try {
            String body;
            if (managementVersion.equals(V3)) {
                requestBody = compact((JsonLdCatalogRequest) input).toString();
            } else {
                requestBody = context.objectMapper().writeValueAsString(input);
            }
            return HttpRequest.newBuilder()
                    .uri(URI.create("%s/dataset/request".formatted(this.url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpRequest.Builder getCatalogRequestBuilder(CatalogRequest input) {
        String requestBody = null;

        try {
            String body;
            if (managementVersion.equals(V3)) {
                requestBody = compact((JsonLdCatalogRequest) input).toString();
            } else {
                requestBody = context.objectMapper().writeValueAsString(input);
            }
            return HttpRequest.newBuilder()
                    .uri(URI.create("%s/request".formatted(this.url)))
                    .header("content-type", "application/json")
                    .POST(ofString(requestBody));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Catalog getCatalog(JsonArray array) {
        return JsonLdCatalog.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Dataset getDataset(JsonArray array) {
        return JsonLdDataset.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Function<InputStream, Catalog> deserializeCatalog() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, PojoCatalog.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<InputStream, Dataset> deserializeDataset() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, PojoDataset.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
