package io.thinkit.edc.client.connector.model;

import java.util.Map;

public record VerificationMethod(
        String controller, String id, Map<String, Object> publicKeyJwk, String publicKeyMultibase, String type) {}
