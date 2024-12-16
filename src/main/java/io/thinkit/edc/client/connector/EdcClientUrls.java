package io.thinkit.edc.client.connector;

import io.thinkit.edc.client.connector.resource.EdcResource;

/**
 * Represent the base EDC api endpoints to be used in the {@link EdcResource}
 */
public record EdcClientUrls(String management, String observability, String catalogCache, String identityUrl) {}
