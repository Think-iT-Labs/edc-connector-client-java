package io.thinkit.edc.client.connector;

/**
 * Represent a {@link EdcResource} constructor, with a {@link EdcClientContext} passed.
 */
public interface ResourceCreator {
    EdcResource create(EdcClientContext context);
}
