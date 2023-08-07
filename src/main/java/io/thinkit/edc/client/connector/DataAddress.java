package io.thinkit.edc.client.connector;

import jakarta.json.JsonObject;

import java.util.Map;

import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static java.util.stream.Collectors.toMap;

public class DataAddress {

    private final JsonObject raw;

    public DataAddress(JsonObject raw) {
        this.raw = raw;
    }

    public String type() {
        return raw.getJsonArray(EDC_NAMESPACE + "type").getJsonObject(0).getString("@value");
    }

    public Map<String, String> properties() {
        return getProperties();
    }

    private Map<String, String> getProperties() {
        return raw.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, it -> it.getValue().asJsonArray().getJsonObject(0).getString("@value")));
    }
}
