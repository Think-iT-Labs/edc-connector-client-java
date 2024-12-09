package io.thinkit.edc.client.connector.model;

import java.util.Map;

public record KeyDescriptor(
        boolean active,
        Map<String, Object> keyGeneratorParams,
        String keyId,
        String privateKeyAlias,
        Map<String, Object> publicKeyJwk,
        String publicKeyPem,
        String resourceId,
        String type) {}
