package io.thinkit.edc.client.connector;

import io.thinkit.edc.client.connector.resource.EdcResource;

/**
 * Represent a {@link EdcResource} constructor, with a {@link EdcClientContext} passed.
 */
public interface ResourceCreator {
    EdcResource create(EdcClientContext context);
}
