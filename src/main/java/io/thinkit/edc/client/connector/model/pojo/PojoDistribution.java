package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.thinkit.edc.client.connector.model.Distribution;
import io.thinkit.edc.client.connector.model.Service;

public class PojoDistribution implements Distribution {

    @JsonProperty("@id")
    private String id;

    @JsonProperty("accessService")
    @JsonDeserialize(using = PojoServiceDeserializer.class)
    private PojoService accessService;

    @JsonProperty("format")
    private String format;

    @Override
    public String id() {
        return id;
    }

    @Override
    public Service accessService() {
        return accessService;
    }

    @Override
    public String format() {
        return format;
    }
}
