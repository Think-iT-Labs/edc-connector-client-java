package io.thinkit.edc.client.connector.model;

public interface ContractAgreement {
    String id();

    String providerId();

    String consumerId();

    String assetId();

    long contractSigningDate();

    Policy policy();
}
