package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.ODRL_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.VALUE;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.List;

public class Policy {
    private final JsonObject raw;

    public Policy(JsonObject raw) {
        this.raw = raw;
    }

    public String uid() {
        return raw.getJsonArray("uid").getJsonObject(0).getString(VALUE);
    }

    public List<JsonObject> permission() {
        return raw.getJsonArray(ODRL_NAMESPACE + "permission").stream()
                .map(JsonValue::asJsonObject)
                .toList();
    }
}
