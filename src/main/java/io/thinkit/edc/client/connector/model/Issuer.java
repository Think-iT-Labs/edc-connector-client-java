package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.Map;

public class Issuer {

    private static final String TYPE_ISSUER = "Issuer";
    private static final String ISSUER_ADDITIONAL_PROPERTIES = "additionalProperties";
    private final JsonObject raw;

    public Issuer(JsonObject raw) {
        this.raw = raw;
    }

    public String id() {
        return raw.getString("id");
    }

    public Properties additionalProperties() {
        return new Properties(raw.getJsonObject(ISSUER_ADDITIONAL_PROPERTIES));
    }

    public JsonObject raw() {
        return raw;
    }

    public static class Builder {
        public final JsonObjectBuilder builder = createObjectBuilder();

        public static Builder newInstance() {
            return new Builder();
        }

        public Issuer build() {
            return new Issuer(builder.add(TYPE, TYPE_ISSUER).build());
        }

        public Builder id(String id) {
            builder.add("id", id);
            return this;
        }

        public Builder additionalProperties(Map<String, ?> properties) {
            var propertiesBuilder = Properties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(ISSUER_ADDITIONAL_PROPERTIES, propertiesBuilder.build().raw());
            return this;
        }
    }
}
