package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.Dataset;
import io.thinkit.edc.client.connector.model.Distribution;
import io.thinkit.edc.client.connector.model.Policy;
import java.util.List;

public class PojoDataset implements Dataset {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("hasPolicy")
    private PojoPolicy hasPolicy;

    @JsonProperty("distribution")
    private List<PojoDistribution> distribution;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Policy hasPolicy() {
        return hasPolicy;
    }

    @Override
    public List<? extends Distribution> distribution() {
        return distribution != null ? distribution : List.of();
    }
}
