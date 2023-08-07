package io.thinkit.edc.client.connector;

import jakarta.json.JsonObject;

import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.ID;
import static io.thinkit.edc.client.connector.Constants.VALUE;

public class Asset {
    private final JsonObject raw;

    public Asset(JsonObject raw) {
        this.raw = raw;
    }

    public String id() {
        return raw.getString(ID);
    }

    public Properties properties() {
        return new Properties(raw.getJsonArray(EDC_NAMESPACE + "properties").getJsonObject(0));
    }

    public Properties privateProperties() {
        return new Properties(raw.getJsonArray(EDC_NAMESPACE + "privateProperties").getJsonObject(0));
    }

    public DataAddress dataAddress() {
        return new DataAddress(raw.getJsonArray(EDC_NAMESPACE + "dataAddress").getJsonObject(0));
    }

    public long createdAt() {
        return raw.getJsonArray(EDC_NAMESPACE + "createdAt").getJsonObject(0).getJsonNumber(VALUE).longValue();
    }
}
