package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.*;
import java.util.List;

public class PojoCatalog implements Catalog {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("dataset")
    private List<PojoDataset> dataset;

    @JsonProperty("service")
    private List<PojoService> service;

    @JsonProperty("participantId")
    private String participantId;

    @JsonProperty("distribution")
    private List<PojoDistribution> distribution;

    @Override
    public String id() {
        return id;
    }

    @Override
    public List<? extends Dataset> dataset() {
        return dataset != null ? dataset : List.of();
    }

    @Override
    public String participantId() {
        return participantId;
    }

    @Override
    public List<? extends Service> service() {
        return service != null ? service : List.of();
    }
}
