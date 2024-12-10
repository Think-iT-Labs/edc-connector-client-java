package io.thinkit.edc.client.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.function.UnaryOperator;

/**
 * Provides base services to be used in a {@link EdcResource} component
 */
public record EdcClientContext(
        EdcClientUrls edcClientUrls,
        ObjectMapper objectMapper,
        HttpClient httpClient,
        UnaryOperator<HttpRequest.Builder> interceptor) {}
