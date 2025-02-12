package io.thinkit.edc.client.connector.resource.catalog;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.resource.EdcResource;

public class CatalogCacheResource extends EdcResource {

    protected final String catalogCacheUrl;

    protected CatalogCacheResource(EdcClientContext context) {
        super(context);
        catalogCacheUrl = context.urls().catalogCache();
        if (catalogCacheUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate %s client without the catalog url"
                    .formatted(getClass().getSimpleName()));
        }
    }
}
