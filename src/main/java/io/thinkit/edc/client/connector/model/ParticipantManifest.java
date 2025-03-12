package io.thinkit.edc.client.connector.model;

import java.util.List;
import java.util.Map;

public record ParticipantManifest(
        boolean active,
        Map<String, Object> additionalProperties,
        String did,
        KeyDescriptor key,
        String participantContextId,
        List<String> roles,
        List<ServiceInput> serviceEndpoints) {}
