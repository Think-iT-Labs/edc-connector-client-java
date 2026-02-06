package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;

import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.DataAddress;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.Map;

public class JsonLdAsset extends JsonLdObject implements Asset {

    private static final String TYPE_ASSET = EDC_NAMESPACE + "Asset";
    private static final String ASSET_PROPERTIES = EDC_NAMESPACE + "properties";
    private static final String ASSET_PRIVATE_PROPERTIES = EDC_NAMESPACE + "privateProperties";
    private static final String ASSET_DATA_ADDRESS = EDC_NAMESPACE + "dataAddress";
    private static final String ASSET_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private JsonLdAsset(JsonObject raw) {
        super(raw);
    }

    public JsonLdProperties properties() {
        return new JsonLdProperties(object(ASSET_PROPERTIES));
    }

    public JsonLdProperties privateProperties() {
        return new JsonLdProperties(object(ASSET_PRIVATE_PROPERTIES));
    }

    public DataAddress dataAddress() {
        return new JsonLdDataAddress(object(ASSET_DATA_ADDRESS));
    }

    public long createdAt() {
        return longValue(ASSET_CREATED_AT);
    }

    public static class Builder extends AbstractBuilder<JsonLdAsset, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public JsonLdAsset build() {
            return new JsonLdAsset(builder.add(TYPE, TYPE_ASSET).build());
        }

        public Builder properties(Map<String, ?> properties) {
            var propertiesBuilder = JsonLdProperties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(ASSET_PROPERTIES, propertiesBuilder.build().raw());
            return this;
        }

        public Builder privateProperties(Map<String, ?> properties) {
            var propertiesBuilder = JsonLdProperties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(ASSET_PRIVATE_PROPERTIES, propertiesBuilder.build().raw());
            return this;
        }

        public Builder dataAddress(Map<String, ?> properties) {
            var propertiesBuilder = JsonLdDataAddress.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            var build = propertiesBuilder.build();
            builder.add(ASSET_DATA_ADDRESS, build.raw());
            return this;
        }
    }
}
