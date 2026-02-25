package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.ContractAgreement;
import io.thinkit.edc.client.connector.model.Policy;

public class PojoContractAgreement implements ContractAgreement {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("providerId")
    private String providerId;

    @JsonProperty("consumerId")
    private String consumerId;

    @JsonProperty("assetId")
    private String assetId;

    @JsonProperty("contractSigningDate")
    private long contractSigningDate;

    @JsonProperty("policy")
    private Policy policy;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String providerId() {
        return providerId;
    }

    @Override
    public String consumerId() {
        return consumerId;
    }

    @Override
    public String assetId() {
        return assetId;
    }

    @Override
    public long contractSigningDate() {
        return contractSigningDate;
    }

    @Override
    public Policy policy() {
        return policy;
    }
}
