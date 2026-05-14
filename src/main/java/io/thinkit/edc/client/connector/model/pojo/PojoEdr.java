package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.Edr;

public class PojoEdr implements Edr {

    @JsonProperty("transferProcessId")
    private String transferProcessId;

    @JsonProperty("agreementId")
    private String agreementId;

    @JsonProperty("contractNegotiationId")
    private String contractNegotiationId;

    @JsonProperty("assetId")
    private String assetId;

    @JsonProperty("providerId")
    private String providerId;

    @JsonProperty("createdAt")
    private long createdAt;

    @Override
    public String transferProcessId() {
        return transferProcessId;
    }

    @Override
    public String agreementId() {
        return agreementId;
    }

    @Override
    public String contractNegotiationId() {
        return contractNegotiationId;
    }

    @Override
    public String assetId() {
        return assetId;
    }

    @Override
    public String providerId() {
        return providerId;
    }

    @Override
    public long createdAt() {
        return createdAt;
    }
}
