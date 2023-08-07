package io.thinkit.edc.client.connector;

import jakarta.json.JsonObject;

import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.VALUE;

public class DataAddress {

    private final JsonObject raw;

    public DataAddress(JsonObject raw) {
        this.raw = raw;
    }

    public String type() {
        return raw.getJsonArray(EDC_NAMESPACE + "type").getJsonObject(0).getString(VALUE);
    }

    public Properties properties() {
        return new Properties(raw);
    }
}
