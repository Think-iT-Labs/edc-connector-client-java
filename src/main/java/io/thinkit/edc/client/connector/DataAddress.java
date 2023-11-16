package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class DataAddress extends JsonLdObject {
    private static final String TYPE_DATA_ADDRESS = EDC_NAMESPACE + "DataAddress";

    public DataAddress(JsonObject raw) {
        super(raw);
    }

    public String type() {
        return stringValue(EDC_NAMESPACE + "type");
    }

    public Properties properties() {
        return new Properties(this.raw());
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder().add(TYPE, TYPE_DATA_ADDRESS);
        ;

        public static DataAddress.Builder newInstance() {
            return new DataAddress.Builder();
        }

        public DataAddress build() {
            return new DataAddress(builder.build());
        }

        public DataAddress.Builder type(String type) {
            builder.add(
                    EDC_NAMESPACE + "type",
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, type)));
            return this;
        }

        public DataAddress.Builder raw(JsonObject raw) {
            builder.addAll(createObjectBuilder(raw));
            return this;
        }
    }
}
