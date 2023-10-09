package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.VALUE;

import jakarta.json.JsonObject;

public class Constraint {

    private final JsonObject raw;

    public Constraint(JsonObject raw) {
        this.raw = raw;
    }

    public String leftOperand() {
        return raw.getJsonArray("leftOperand").getJsonObject(0).getString(VALUE);
    }

    public String operator() {
        return raw.getJsonArray("operator").getJsonObject(0).getString(VALUE);
    }

    public String rightOperand() {
        return raw.getJsonArray("rightOperand").getJsonObject(0).getString(VALUE);
    }

    public String comment() {
        return raw.getJsonArray("comment").getJsonObject(0).getString(VALUE);
    }
}
