package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;

import jakarta.json.JsonObject;

public class PolicyDefinition {
    private final JsonObject raw;

    public PolicyDefinition(JsonObject raw) {
        this.raw = raw;
    }

    public String id() {
        return raw.getString(ID);
    }

    public Policy policy() {
        return new Policy(raw.getJsonArray("odrl:policy").getJsonObject(0));
    }

    public long createdAt() {
        return raw.getJsonArray("createdAt")
                .getJsonObject(0)
                .getJsonNumber(VALUE)
                .longValue();
    }
}
