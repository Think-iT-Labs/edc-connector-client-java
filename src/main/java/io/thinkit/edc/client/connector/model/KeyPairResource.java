package io.thinkit.edc.client.connector.model;

import java.math.BigInteger;

public record KeyPairResource(
        boolean defaultPair,
        String id,
        String groupName,
        String keyContext,
        String keyId,
        String participantContextId,
        String privateKeyAlias,
        BigInteger rotationDuration,
        String serializedPublicKey,
        int state,
        BigInteger timestamp,
        BigInteger useDuration) {}
