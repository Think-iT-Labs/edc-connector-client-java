package io.thinkit.edc.client.connector;

/**
 * Represent an EDC endpoint resource
 */
public abstract class EdcResource {

    protected final EdcClientContext context;

    protected EdcResource(EdcClientContext context) {
        this.context = context;
    }
}
