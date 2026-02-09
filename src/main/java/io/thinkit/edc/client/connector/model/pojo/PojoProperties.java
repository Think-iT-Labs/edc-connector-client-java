package io.thinkit.edc.client.connector.model.pojo;

import io.thinkit.edc.client.connector.model.Properties;
import jakarta.json.JsonValue;
import java.util.HashMap;
import java.util.Map;

public class PojoProperties implements Properties {

    private final Map<String, Object> properties = new HashMap<>();

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public String getString(String key) {
        return properties.get(key).toString();
    }

    @Override
    public JsonValue raw() {
        throw new IllegalCallerException("Not Implemented for pojo");
    }

    public static class Builder {

        private final PojoProperties properties = new PojoProperties();

        public static Builder newInstance() {
            return new Builder();
        }

        public Properties build() {
            return properties;
        }

        public Builder property(String key, Object value) {
            properties.properties.put(key, value);
            return this;
        }
    }
}
