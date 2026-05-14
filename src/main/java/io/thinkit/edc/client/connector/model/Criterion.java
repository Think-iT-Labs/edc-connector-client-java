package io.thinkit.edc.client.connector.model;

public interface Criterion {

    String operator();

    Object operandLeft();

    Object operandRight();
}
