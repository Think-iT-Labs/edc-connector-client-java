package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.ID;
import static io.thinkit.edc.client.connector.Constants.VALUE;
import static io.thinkit.edc.client.connector.Constants.VOCAB;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.Map;

public class Asset {

    private static final String ASSET_PROPERTIES = EDC_NAMESPACE + "properties";
    private static final String ASSET_PRIVATE_PROPERTIES = EDC_NAMESPACE + "privateProperties";
    private static final String ASSET_DATA_ADDRESS = EDC_NAMESPACE + "dataAddress";

    private final JsonObject raw;

    public Asset(JsonObject raw) {
        this.raw = raw;
    }

    public String id() {
        return raw.getString(ID);
    }

    public Properties properties() {
        return new Properties(raw.getJsonArray(ASSET_PROPERTIES).getJsonObject(0));
    }

    public Properties privateProperties() {
        return new Properties(raw.getJsonArray(ASSET_PRIVATE_PROPERTIES).getJsonObject(0));
    }

    public DataAddress dataAddress() {
        return new DataAddress(raw.getJsonArray(ASSET_DATA_ADDRESS).getJsonObject(0));
    }

    public long createdAt() {
        return raw.getJsonArray(EDC_NAMESPACE + "createdAt")
                .getJsonObject(0)
                .getJsonNumber(VALUE)
                .longValue();
    }

    public JsonObject raw() {
        return raw;
    }

    public static class Builder {

        private final JsonObjectBuilder raw = createObjectBuilder()
                .add(Constants.CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE));

        public static Builder newInstance() {
            return new Builder();
        }

        public Asset build() {
            return new Asset(raw.build());
        }

        public Builder id(String id) {
            raw.add(ID, id);
            return this;
        }

        public Builder properties(Map<String, ?> properties) {
            var builder = Properties.Builder.newInstance();
            properties.forEach(builder::property);
            raw.add(ASSET_PROPERTIES, builder.build().raw());
            return this;
        }

        public Builder privateProperties(Map<String, ?> properties) {
            var builder = Properties.Builder.newInstance();
            properties.forEach(builder::property);
            raw.add(ASSET_PRIVATE_PROPERTIES, builder.build().raw());
            return this;
        }

        public Builder dataAddress(Map<String, ?> properties) {
            var builder = Properties.Builder.newInstance();
            properties.forEach(builder::property);
            raw.add(ASSET_DATA_ADDRESS, builder.build().raw());
            return this;
        }
    }
}
