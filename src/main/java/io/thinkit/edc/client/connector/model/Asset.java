package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
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

    public static class Builder extends AbstractBuilder<Asset, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public Asset build() {
            return new Asset(builder.add(TYPE, TYPE_ASSET).build());
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
    }
}
