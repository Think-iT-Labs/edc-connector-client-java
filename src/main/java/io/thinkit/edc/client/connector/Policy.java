package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.VALUE;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.List;

public class Policy {
    private final JsonObject raw;

    public Policy(JsonObject raw) {
        this.raw = raw;
    }

    public String getStringValue(String key) {
        return raw.getJsonArray(key).getJsonObject(0).getString(VALUE);
    }

    List<JsonObject> getList(String key) {
        return raw.getJsonArray(key).stream().map(JsonValue::asJsonObject).toList();
    }
}
