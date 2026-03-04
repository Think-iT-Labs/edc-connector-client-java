package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.Catalog;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdCatalog;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
import io.thinkit.edc.client.connector.resource.catalog.CatalogCacheResource;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.JsonValue;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CatalogCache extends CatalogCacheResource {
    private final String url;

    public CatalogCache(EdcClientContext context) {
        super(context);
        url = "%s/v1alpha/catalog".formatted(catalogCacheUrl);
    }

    public Result<List<Catalog>> query(QuerySpec query) {
        var requestBuilder = queryCatalogsRequestBuilder(query);

        return handleOutput(context.httpClient().send(requestBuilder));
    }

    public CompletableFuture<Result<List<Catalog>>> queryAsync(QuerySpec query) {
        var requestBuilder = queryCatalogsRequestBuilder(query);

        return context.httpClient().sendAsync(requestBuilder).thenApply(this::handleOutput);
    }

    private Result<List<Catalog>> handleOutput(Result<InputStream> output) {
        return output.map(JsonLdUtil::expand).map(it -> it.stream()
                .map(JsonValue::asJsonObject)
                .map(a -> JsonLdCatalog.Builder.newInstance().raw(a).build())
                .map(Catalog.class::cast)
                .toList());
    }

    private HttpRequest.Builder queryCatalogsRequestBuilder(QuerySpec query) {
        var requestBody = compact((JsonLdQuerySpec) query);

        return HttpRequest.newBuilder()
                .uri(URI.create("%s/query".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }
}
