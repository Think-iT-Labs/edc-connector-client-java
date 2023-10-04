package io.thinkit.edc.client.connector;

public record Criterion(String operator, Object operandLeft, Object operandRight) {}
