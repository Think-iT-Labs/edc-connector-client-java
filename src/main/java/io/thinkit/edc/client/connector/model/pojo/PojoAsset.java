package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.DataAddress;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdProperties;
import java.util.Map;

public class PojoAsset implements Asset {

    @JsonProperty("@id")
    private String id;

    @JsonProperty("properties")
    private Map<String, Object> properties;

    @JsonProperty("privateProperties")
    private Map<String, Object> privateProperties;

    @JsonProperty("dataAddress")
    private PojoDataAddress dataAddress;

    @JsonProperty("createdAt")
    private long createdAt;

    @Override
    public String id() {
        return "id";
    }

    @Override
    public JsonLdProperties properties() {
        var builder = JsonLdProperties.Builder.newInstance();
        properties.forEach(builder::property);
        return builder.build();
    }

    @Override
    public JsonLdProperties privateProperties() {
        var builder = JsonLdProperties.Builder.newInstance();
        privateProperties.forEach(builder::property);
        return builder.build();
    }

    @Override
    public DataAddress dataAddress() {
        return dataAddress;
    }

    @Override
    public long createdAt() {
        return createdAt;
    }
}
