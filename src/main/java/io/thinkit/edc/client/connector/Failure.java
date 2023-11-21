package io.thinkit.edc.client.connector;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.List;

public class Failure {

    private final JsonObject raw;

    public Failure(JsonObject raw) {
        this.raw = raw;
    }

    public String failureDetail() {
        return raw.getString("failureDetail");
    }

    public List<String> messages() {
        return raw.getJsonArray("messages").stream().map(JsonValue::toString).toList();
    }
}
