package io.thinkit.edc.client.connector;

public record QuerySpec(int offset, int limit, String sortOrder, String sortField, CriterionInput[] filterExpression) {}
