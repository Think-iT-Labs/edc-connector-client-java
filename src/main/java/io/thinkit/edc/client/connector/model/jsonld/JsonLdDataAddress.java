package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;
import static io.thinkit.edc.client.connector.utils.Constants.VALUE;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.DataAddress;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class JsonLdDataAddress extends JsonLdObject implements DataAddress {
    private static final String TYPE_DATA_ADDRESS = EDC_NAMESPACE + "DataAddress";

    public JsonLdDataAddress(JsonObject raw) {
        super(raw);
    }

    public String type() {
        return stringValue(EDC_NAMESPACE + "type");
    }

    public JsonLdProperties properties() {
        return new JsonLdProperties(this.raw());
    }

    public static class Builder extends AbstractBuilder<JsonLdDataAddress, Builder> {

        public static JsonLdDataAddress.Builder newInstance() {
            return new JsonLdDataAddress.Builder();
        }

        public JsonLdDataAddress build() {
            return new JsonLdDataAddress(builder.add(TYPE, TYPE_DATA_ADDRESS).build());
        }

        public JsonLdDataAddress.Builder type(String type) {
            builder.add(
                    EDC_NAMESPACE + "type",
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, type)));
            return this;
        }

        public Builder property(String key, Object value) {
            // TODO: value types should be managed correctly
            builder.add(key, createArrayBuilder().add(createObjectBuilder().add(VALUE, value.toString())));
            return this;
        }
    }
}
