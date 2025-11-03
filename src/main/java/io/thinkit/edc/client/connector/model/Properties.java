package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;
import static io.thinkit.edc.client.connector.utils.Constants.VALUE;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.util.Map;
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

    public JsonValue raw() {
        return raw;
    }

    public static class Builder {

        private final JsonObjectBuilder raw = createObjectBuilder();

        public static Builder newInstance() {
            return new Builder();
        }

        public Properties build() {
            return new Properties(raw.build());
        }

        public Builder type(String type) {
            raw.add(TYPE, type);
            return this;
        }

        public Builder property(String key, Object value) {
            raw.add(key, toJsonValue(value));
            return this;
        }

        private JsonValue toJsonValue(Object value) {
            if (value instanceof Map<?, ?> map) {
                var builder = Builder.newInstance();
                map.forEach((k, v) -> builder.property((String) k, v));
                var properties = builder.build();
                return createArrayBuilder().add(properties.raw).build();
            } else if (value instanceof String stringValue) {
                return Json.createValue(stringValue);
            } else {
                return createArrayBuilder()
                        .add(createObjectBuilder().add(VALUE, value.toString()))
                        .build(); // TODO: watch out toString
            }
        }
    }
}
