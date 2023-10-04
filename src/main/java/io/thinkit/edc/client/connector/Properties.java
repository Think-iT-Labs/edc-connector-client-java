package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.VALUE;

import jakarta.json.JsonObject;
import java.util.Optional;

public class Properties {
    private final JsonObject raw;

    public Properties(JsonObject raw) {
        this.raw = raw;
    }

    public int size() {
        return raw.size();
    }

    public String getString(String key) {
        var property =
                Optional.ofNullable(raw.getJsonArray(key)).orElseGet(() -> raw.getJsonArray(EDC_NAMESPACE + key));

        return Optional.ofNullable(property)
                .map(it -> it.getJsonObject(0))
                .map(it -> it.getString(VALUE))
                .orElse(null);
    }
}
