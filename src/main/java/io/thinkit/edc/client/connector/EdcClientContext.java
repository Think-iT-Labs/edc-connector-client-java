package io.thinkit.edc.client.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.thinkit.edc.client.connector.resource.EdcResource;
import io.thinkit.edc.client.connector.services.EdcApiHttpClient;

/**
 * Provides base services to be used in a {@link EdcResource} component
 */
public record EdcClientContext(EdcClientUrls urls, ObjectMapper objectMapper, EdcApiHttpClient httpClient) {}
