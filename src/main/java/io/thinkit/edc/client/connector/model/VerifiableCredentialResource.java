package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

import java.util.Map;

public record VerifiableCredentialResource(
        String id,
        String credentialStatus,
        String holderId,
        Map<String, Object> issuancePolicy,
        String issuerId,
        String participantId,
        Map<String, Object> reissuancePolicy,
        int state,
        String timeOfLastStatusUpdate,
        float timestamp,
        VerifiableCredentialContainer verifiableCredential) {}
