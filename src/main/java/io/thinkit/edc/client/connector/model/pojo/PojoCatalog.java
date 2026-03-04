package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.*;
import java.util.List;

public class PojoCatalog implements Catalog {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("dataset")
    private Dataset dataset;

    @JsonProperty("service")
    private Service service;

    @JsonProperty("participantId")
    private String participantId;

    @JsonProperty("distribution")
    private List<PojoDistribution> distribution;

    @Override
    public String id() {
        return id;
    }

    @Override
    public Dataset dataset() {
        return dataset;
    }

    @Override
    public String participantId() {
        return participantId;
    }

    @Override
    public Service service() {
        return service;
    }
}
