package io.thinkit.edc.client.connector.model;

public interface PolicyDefinition {
    String id();

    Policy policy();

    long createdAt();
}
