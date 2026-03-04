package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.Distribution;

public class PojoDistribution implements Distribution {

    @JsonProperty("@id")
    private String id;

    @JsonProperty("accessService")
    private String accessService;

    @JsonProperty("format")
    private String format;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String accessService() {
        return accessService;
    }

    @Override
    public String format() {
        return format;
    }
}
