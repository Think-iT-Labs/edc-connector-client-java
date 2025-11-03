package io.thinkit.edc.client.connector.resource;

import io.thinkit.edc.client.connector.EdcClientContext;

/**
 * Represent an EDC endpoint resource
 */
public abstract class EdcResource {

    protected final EdcClientContext context;

    protected EdcResource(EdcClientContext context) {
        this.context = context;
    }
}
