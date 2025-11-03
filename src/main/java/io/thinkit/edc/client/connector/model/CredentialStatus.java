package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

import java.util.Map;

public record CredentialStatus(String id, String type, Map<String, Object> additionalProperties) {}
