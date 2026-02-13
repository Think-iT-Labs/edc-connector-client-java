package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;

import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdDataPlaneInstance;
import io.thinkit.edc.client.connector.model.pojo.PojoDataPlaneInstance;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Dataplanes extends ManagementResource {
    private final String url;

    public Dataplanes(EdcClientContext context) {
        super(context);
        url = "%s/dataplanes".formatted(managementUrl);
    }

    public Result<List<DataPlaneInstance>> get() {
        var requestBuilder = getRequestBuilder();
        Function<Result<InputStream>, Result<List<DataPlaneInstance>>> function = managementVersion.equals(V3)
                ? result -> result.map(JsonLdUtil::expand).map(this::getDataPlaneInstances)
                : result -> result.map(deserializeDataPlaneInstances());

        return function.apply(context.httpClient().send(requestBuilder));
    }

    public CompletableFuture<Result<List<DataPlaneInstance>>> getAsync() {
        var requestBuilder = getRequestBuilder();
        Function<Result<InputStream>, Result<List<DataPlaneInstance>>> function = managementVersion.equals(V3)
                ? result -> result.map(JsonLdUtil::expand).map(this::getDataPlaneInstances)
                : result -> result.map(deserializeDataPlaneInstances());

        return context.httpClient().sendAsync(requestBuilder).thenApply(function);
    }

    private HttpRequest.Builder getRequestBuilder() {
        return HttpRequest.newBuilder().uri(URI.create(this.url)).GET();
    }

    private List<DataPlaneInstance> getDataPlaneInstances(JsonArray array) {
        return array.stream()
                .map(s -> JsonLdDataPlaneInstance.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .map(DataPlaneInstance.class::cast)
                .toList();
    }

    private Function<InputStream, List<DataPlaneInstance>> deserializeDataPlaneInstances() {
        return stream -> {
            try {
                // 1. Read the stream into a byte array so we can use it twice
                byte[] bytes = stream.readAllBytes();

                // 2. Debug print: Convert bytes to String to see the raw JSON
                System.out.println("DEBUG - Raw JSON: " + new String(bytes, StandardCharsets.UTF_8));

                // 3. Deserialize from the byte array instead of the consumed stream
                return context
                        .objectMapper()
                        .readValue(bytes, new TypeReference<List<PojoDataPlaneInstance>>() {})
                        .stream()
                        .map(DataPlaneInstance.class::cast)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException("Failed to deserialize DataPlaneInstances", e);
            }
        };
    }
}
