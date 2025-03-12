package io.thinkit.edc.client.connector.model;

import java.math.BigInteger;
import java.util.List;

public record ParticipantContext(
        String apiTokenAlias,
        BigInteger createdAt,
        String did,
        BigInteger lastModified,
        String participantContextId,
        List<String> roles,
        int state) {}
