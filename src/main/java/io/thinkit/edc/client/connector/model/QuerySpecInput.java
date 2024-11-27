package io.thinkit.edc.client.connector.model;


public record QuerySpecInput(
        int offset, int limit, String sortOrder, String sortField, CriterionInput[] filterExpression) {}
