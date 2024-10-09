package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.Map;

public class CredentialStatus {

    private static final String TYPE_CREDENTIAL_STATUS = "CredentialStatus";
    private static final String CREDENTIAL_STATUS_TYPE = "type";
    private static final String CREDENTIAL_STATUS_ADDITIONAL_PROPERTIES = "additionalProperties";
    private final JsonObject raw;

    public CredentialStatus(JsonObject raw) {
        this.raw = raw;
    }

    public String Type() {
        return raw.getString(CREDENTIAL_STATUS_TYPE);
    }

    public String id() {
        return raw.getString("id");
    }

    public Properties additionalProperties() {
        return new Properties(raw.getJsonObject(CREDENTIAL_STATUS_ADDITIONAL_PROPERTIES));
    }

    public JsonObject raw() {
        return raw;
    }

    public static class Builder {
        public final JsonObjectBuilder builder = createObjectBuilder();

        public static Builder newInstance() {
            return new Builder();
        }

        public CredentialStatus build() {
            return new CredentialStatus(builder.build());
        }

        public Builder type(String type) {
            builder.add(CREDENTIAL_STATUS_TYPE, type);
            return this;
        }

        public Builder id(String id) {
            builder.add("id", id);
            return this;
        }

        public Builder additionalProperties(Map<String, ?> properties) {
            var propertiesBuilder = Properties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(
                    CREDENTIAL_STATUS_ADDITIONAL_PROPERTIES,
                    propertiesBuilder.build().raw());
            return this;
        }

        public Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
