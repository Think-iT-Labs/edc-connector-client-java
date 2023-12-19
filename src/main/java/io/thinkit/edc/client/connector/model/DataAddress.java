package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

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

    public static class Builder extends AbstractBuilder<DataAddress, DataAddress.Builder> {

        public static DataAddress.Builder newInstance() {
            return new DataAddress.Builder();
        }

        public DataAddress build() {
            return new DataAddress(builder.add(TYPE, TYPE_DATA_ADDRESS).build());
        }

        public DataAddress.Builder type(String type) {
            builder.add(
                    EDC_NAMESPACE + "type",
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, type)));
            return this;
        }
    }
}
