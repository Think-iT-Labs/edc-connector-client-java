package io.thinkit.edc.client.connector.model;

import java.util.Map;

public record VerifiableCredentialManifest(
        String id,
        Map<String, Object> issuancePolicy,
        String participantContextId,
        Map<String, Object> reissuancePolicy,
        VerifiableCredentialContainer verifiableCredential) {}
