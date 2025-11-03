package io.thinkit.edc.client.connector.resource.management;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.resource.EdcResource;

public class ManagementResource extends EdcResource {

    protected final String managementUrl;

    protected ManagementResource(EdcClientContext context) {
        super(context);
        managementUrl = context.urls().management();
        if (managementUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate %s client without the management url"
                    .formatted(getClass().getSimpleName()));
        }
    }
}
