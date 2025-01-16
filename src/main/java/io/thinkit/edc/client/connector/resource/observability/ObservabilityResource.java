package io.thinkit.edc.client.connector.resource.observability;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.resource.EdcResource;

public class ObservabilityResource extends EdcResource {

    protected final String observabilityUrl;

    protected ObservabilityResource(EdcClientContext context) {
        super(context);
        observabilityUrl = context.urls().observability();
        if (observabilityUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate %s client without the observability url"
                    .formatted(getClass().getSimpleName()));
        }
    }
}
