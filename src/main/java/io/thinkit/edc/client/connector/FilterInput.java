package io.thinkit.edc.client.connector;

import java.util.Map;

public record FilterInput(
        String type,
        int offset,
        int limit,
        String sortOrder,
        String sortField,
        String[] filterExpression
) {

}
