package io.thinkit.edc.client.connector.model;

public interface Edr {

    String transferProcessId();

    String agreementId();

    String contractNegotiationId();

    String assetId();

    String providerId();

    long createdAt();
}
