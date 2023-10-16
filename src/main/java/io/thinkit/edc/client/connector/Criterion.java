package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;

import jakarta.json.JsonObject;

public class Criterion {
    private final JsonObject raw;

    public Criterion(JsonObject raw) {
        this.raw = raw;
    }

    public String operator() {
        return raw.getJsonArray(EDC_NAMESPACE + "operator").getJsonObject(0).getString(VALUE);
    }

    public Object operandLeft() {
        return raw.getJsonArray(EDC_NAMESPACE + "operandLeft").getJsonObject(0);
    }

    public Object operandRight() {
        return raw.getJsonArray(EDC_NAMESPACE + "operandRight").getJsonObject(0);
    }
}
