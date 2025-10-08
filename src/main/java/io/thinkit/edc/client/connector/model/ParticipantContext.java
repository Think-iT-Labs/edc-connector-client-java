package io.thinkit.edc.client.connector.model;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public record ParticipantContext(
        String apiTokenAlias,
        BigInteger createdAt,
        String did,
        BigInteger lastModified,
        String participantContextId,
        List<String> roles,
        int state,
        Map<String, Object> properties) {}
