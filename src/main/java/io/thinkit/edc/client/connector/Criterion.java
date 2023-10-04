package io.thinkit.edc.client.connector;

import java.util.Map;

public record Criterion(
        String operator,
        Object operandLeft,
        Object operandRight
) {

}
