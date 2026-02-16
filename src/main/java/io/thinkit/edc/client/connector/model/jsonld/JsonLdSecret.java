package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.Secret;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class JsonLdSecret extends JsonLdObject implements Secret {

    private static final String TYPE_SECRET = EDC_NAMESPACE + "Secret";
    private static final String SECRET_VALUE = EDC_NAMESPACE + "value";

    private JsonLdSecret(JsonObject raw) {
        super(raw);
    }

    public String value() {
        return stringValue(SECRET_VALUE);
    }

    public static class Builder extends AbstractBuilder<JsonLdSecret, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public JsonLdSecret build() {
            return new JsonLdSecret(builder.add(TYPE, TYPE_SECRET).build());
        }

        public JsonLdSecret.Builder value(String value) {
            builder.add(
                    SECRET_VALUE, createArrayBuilder().add(createObjectBuilder().add(VALUE, value)));
            return this;
        }
    }
}
