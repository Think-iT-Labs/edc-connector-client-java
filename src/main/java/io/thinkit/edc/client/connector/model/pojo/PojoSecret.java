package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.Secret;

public class PojoSecret implements Secret {

    @JsonProperty("@id")
    private String id;

    @JsonProperty("value")
    private String value;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String value() {
        return value;
    }
}
