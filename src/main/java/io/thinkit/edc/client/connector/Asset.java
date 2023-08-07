package io.thinkit.edc.client.connector;

import jakarta.json.JsonObject;

import java.util.Map;

import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static java.util.stream.Collectors.toMap;

public class Asset {
    private final JsonObject raw;

    public Asset(JsonObject raw) {
        this.raw = raw;
    }

    public String id() {
        return raw.getString("@id");
    }

    public Map<String, Object> properties() {
        return getProperties("https://w3id.org/edc/v0.0.1/ns/properties");
    }

    public Map<String, Object> privateProperties() {
        return getProperties("https://w3id.org/edc/v0.0.1/ns/privateProperties");
    }

    private Map<String, Object> getProperties(String key) {
        return raw.getJsonArray(key).stream()
                .flatMap(it -> it.asJsonObject().entrySet().stream())
                .collect(toMap(Map.Entry::getKey, it -> it.getValue().asJsonArray().getJsonObject(0).getString("@value")));
    }

    public DataAddress dataAddress() {
        return new DataAddress(raw.getJsonArray("https://w3id.org/edc/v0.0.1/ns/dataAddress").getJsonObject(0));
    }

    public long createdAt() {
        return raw.getJsonArray(EDC_NAMESPACE + "createdAt").getJsonObject(0).getJsonNumber("@value").longValue();
    }
}
