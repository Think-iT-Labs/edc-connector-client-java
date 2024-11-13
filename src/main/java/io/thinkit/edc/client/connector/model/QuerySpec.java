package io.thinkit.edc.client.connector.model;

public record QuerySpec(int offset, int limit, String sortOrder, String sortField, CriterionInput[] filterExpression) {}
