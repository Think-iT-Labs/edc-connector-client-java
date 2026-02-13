package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.Secret;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdSecret;
import io.thinkit.edc.client.connector.model.pojo.PojoSecret;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Secrets extends ManagementResource {
    private final String url;

    public Secrets(EdcClientContext context) {
        super(context);
        url = "%s/secrets".formatted(managementUrl);
    }

    public Result<Secret> get(String id) {
        var requestBuilder = getRequestBuilder(id);
        Function<InputStream, Result<Secret>> function = managementVersion.equals(V3)
                ? stream -> Result.succeded(stream).map(JsonLdUtil::expand).map(this::getSecret)
                : stream -> Result.succeded(stream).map(deserializeSecret());

        return context.httpClient().send(requestBuilder).compose(function);
    }

    public CompletableFuture<Result<Secret>> getAsync(String id) {
        var requestBuilder = getRequestBuilder(id);
        Function<Result<InputStream>, Result<Secret>> function = managementVersion.equals(V3)
                ? result -> result.map(JsonLdUtil::expand).map(this::getSecret)
                : result -> result.map(deserializeSecret());

        return context.httpClient().sendAsync(requestBuilder).thenApply(function);
    }

    public Result<String> create(Secret input) {
        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(JsonLdUtil::expand).map(content -> content.getJsonObject(0)
                .getString(ID));
    }

    public CompletableFuture<Result<String>> createAsync(Secret input) {
        var requestBuilder = createRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(JsonLdUtil::expand)
                .map(content -> content.getJsonObject(0).getString(ID)));
    }

    public Result<String> update(Secret input) {
        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient().send(requestBuilder).map(result -> input.id());
    }

    public CompletableFuture<Result<String>> updateAsync(Secret input) {

        var requestBuilder = updateRequestBuilder(input);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> input.id()));
    }

    public Result<String> delete(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return context.httpClient().send(requestBuilder).map(result -> id);
    }

    public CompletableFuture<Result<String>> deleteAsync(String id) {
        var requestBuilder = deleteRequestBuilder(id);

        return context.httpClient().sendAsync(requestBuilder).thenApply(result -> result.map(content -> id));
    }

    private HttpRequest.Builder getRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder createRequestBuilder(Secret input) {
        var requestBody = compact((JsonLdSecret) input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder updateRequestBuilder(Secret input) {
        var requestBody = compact((JsonLdSecret) input);
        return HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("content-type", "application/json")
                .PUT(ofString(requestBody.toString()));
    }

    private HttpRequest.Builder deleteRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .DELETE();
    }

    private Secret getSecret(JsonArray array) {
        return JsonLdSecret.Builder.newInstance().raw(array.getJsonObject(0)).build();
    }

    private Function<InputStream, Secret> deserializeSecret() {
        return stream -> {
            try {
                return context.objectMapper().readValue(stream, PojoSecret.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
