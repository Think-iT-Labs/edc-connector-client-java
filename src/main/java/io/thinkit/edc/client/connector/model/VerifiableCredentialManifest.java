package io.thinkit.edc.client.connector.model;

import java.util.Map;

public record VerifiableCredentialManifest(
        Map<String, Object> issuancePolicy,
        String participantId,
        Map<String, Object> reissuancePolicy,
        VerifiableCredentialContainer verifiableCredential) {}
