package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class Secret extends JsonLdObject {

    private static final String TYPE_SECRET = EDC_NAMESPACE + "Secret";
    private static final String SECRET_VALUE = EDC_NAMESPACE + "value";

    private Secret(JsonObject raw) {
        super(raw);
    }

    public String value() {
        return stringValue(SECRET_VALUE);
    }

    public static class Builder extends AbstractBuilder<Secret, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public Secret build() {
            return new Secret(builder.add(TYPE, TYPE_SECRET).build());
        }

        public Secret.Builder value(String value) {
            builder.add(
                    SECRET_VALUE, createArrayBuilder().add(createObjectBuilder().add(VALUE, value)));
            return this;
        }
    }
}
