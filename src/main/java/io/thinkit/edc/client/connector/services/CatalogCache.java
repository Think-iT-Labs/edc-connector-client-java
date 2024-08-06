package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.model.Catalog;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonValue;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class CatalogCache {
    private final EdcApiHttpClient edcApiHttpClient;
    private final String url;

    public CatalogCache(String url, HttpClient httpClient, UnaryOperator<HttpRequest.Builder> interceptor) {
        edcApiHttpClient = new EdcApiHttpClient(httpClient, interceptor);
        this.url = "%s/v1alpha/catalog".formatted(url);
    }

    public Result<List<Catalog>> query(QuerySpec query) {
        var requestBuilder = queryCatalogsRequestBuilder(query);

        return handleOutput(edcApiHttpClient.send(requestBuilder));
    }

    public CompletableFuture<Result<List<Catalog>>> queryAsync(QuerySpec query) {
        var requestBuilder = queryCatalogsRequestBuilder(query);

        return edcApiHttpClient.sendAsync(requestBuilder).thenApply(this::handleOutput);
    }

    private Result<List<Catalog>> handleOutput(Result<InputStream> output) {
        return output.map(JsonLdUtil::expand).map(it -> it.stream()
                .map(JsonValue::asJsonObject)
                .map(a -> Catalog.Builder.newInstance().raw(a).build())
                .toList());
    }

    private HttpRequest.Builder queryCatalogsRequestBuilder(QuerySpec query) {
        var requestBody = compact(query);

        return HttpRequest.newBuilder()
                .uri(URI.create("%s/query".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }
}
