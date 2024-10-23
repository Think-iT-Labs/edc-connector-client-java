package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

import java.util.Map;

public record Issuer(String id, Map<String, Object> additionalProperties) {}
