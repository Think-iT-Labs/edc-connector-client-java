package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.ContractDefinition;
import java.util.List;

public class PojoContractDefinition implements ContractDefinition {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("accessPolicyId")
    private String accessPolicyId;

    @JsonProperty("contractPolicyId")
    private String contractPolicyId;

    @JsonProperty("assetsSelector")
    private List<PojoCriterion> assetsSelector;

    @JsonProperty("createdAt")
    private long createdAt;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String accessPolicyId() {
        return accessPolicyId;
    }

    @Override
    public String contractPolicyId() {
        return contractPolicyId;
    }

    @Override
    public List<PojoCriterion> assetsSelector() {
        return assetsSelector != null ? assetsSelector : List.of();
    }

    @Override
    public long createdAt() {
        return createdAt;
    }
}
