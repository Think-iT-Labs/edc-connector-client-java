package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.Service;

public class PojoService implements Service {

    @JsonProperty("@id")
    private String id;

    @JsonProperty("terms")
    private String terms;

    @JsonProperty("endpointUrl")
    private String endpointUrl;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String terms() {
        return terms;
    }

    @Override
    public String endpointUrl() {
        return endpointUrl;
    }
}
