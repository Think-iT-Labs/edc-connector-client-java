package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.CONTEXT;
import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.ID;
import static io.thinkit.edc.client.connector.Constants.TYPE;
import static io.thinkit.edc.client.connector.Constants.VOCAB;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.Map;

public class Asset extends JsonLdObject {

    private static final String TYPE_ASSET = EDC_NAMESPACE + "Asset";
    private static final String ASSET_PROPERTIES = EDC_NAMESPACE + "properties";
    private static final String ASSET_PRIVATE_PROPERTIES = EDC_NAMESPACE + "privateProperties";
    private static final String ASSET_DATA_ADDRESS = EDC_NAMESPACE + "dataAddress";
    private static final String ASSET_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private Asset(JsonObject raw) {
        super(raw);
    }

    public Properties properties() {
        return new Properties(object(ASSET_PROPERTIES));
    }

    public Properties privateProperties() {
        return new Properties(object(ASSET_PRIVATE_PROPERTIES));
    }

    public DataAddress dataAddress() {
        return new DataAddress(object(ASSET_DATA_ADDRESS));
    }

    public long createdAt() {
        return longValue(ASSET_CREATED_AT);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_ASSET);

        public static Builder newInstance() {
            return new Builder();
        }

        public Asset build() {
            return new Asset(builder.build());
        }

        public Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public Builder properties(Map<String, ?> properties) {
            var propertiesBuilder = Properties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(ASSET_PROPERTIES, propertiesBuilder.build().raw());
            return this;
        }

        public Builder privateProperties(Map<String, ?> properties) {
            var propertiesBuilder = Properties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(ASSET_PRIVATE_PROPERTIES, propertiesBuilder.build().raw());
            return this;
        }

        public Builder dataAddress(Map<String, ?> properties) {
            var propertiesBuilder = Properties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(ASSET_DATA_ADDRESS, propertiesBuilder.build().raw());
            return this;
        }

        public Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
