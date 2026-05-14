package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.*;
import java.util.List;

public class PojoCatalog implements Catalog {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("dataset")
    private List<PojoDataset> datasets;

    @JsonProperty("service")
    private List<PojoService> services;

    @JsonProperty("participantId")
    private String participantId;

    @JsonProperty("distribution")
    private List<PojoDistribution> distribution;

    @Override
    public String id() {
        return id;
    }

    @Override
    public List<? extends Dataset> datasets() {
        return datasets != null ? datasets : List.of();
    }

    @Override
    public String participantId() {
        return participantId;
    }

    @Override
    public List<? extends Service> services() {
        return services != null ? services : List.of();
    }
}
