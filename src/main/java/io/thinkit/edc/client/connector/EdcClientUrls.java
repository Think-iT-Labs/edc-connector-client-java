package io.thinkit.edc.client.connector;

import io.thinkit.edc.client.connector.resource.EdcResource;
import io.thinkit.edc.client.connector.resource.VersionedApi;

/**
 * Represent the base EDC api endpoints to be used in the {@link EdcResource}
 */
public record EdcClientUrls(
        VersionedApi management, String observability, String catalogCache, String identityUrl, String presentation) {}
