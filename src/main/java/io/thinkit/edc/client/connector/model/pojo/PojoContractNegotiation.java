package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.ContractNegotiation;
import java.util.List;

public class PojoContractNegotiation implements ContractNegotiation {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("counterPartyId")
    private String counterPartyId;

    @JsonProperty("counterPartyAddress")
    private String counterPartyAddress;

    @JsonProperty("state")
    private String state;

    @JsonProperty("contractAgreementId")
    private String contractAgreementId;

    @JsonProperty("errorDetail")
    private String errorDetail;

    @JsonProperty("callbackAddresses")
    private List<PojoCallbackAddress> callbackAddresses;

    @JsonProperty("createdAt")
    private long createdAt;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String protocol() {
        return protocol;
    }

    @Override
    public String counterPartyId() {
        return counterPartyId;
    }

    @Override
    public String counterPartyAddress() {
        return counterPartyAddress;
    }

    @Override
    public String state() {
        return state;
    }

    @Override
    public String contractAgreementId() {
        return contractAgreementId;
    }

    @Override
    public String errorDetail() {
        return errorDetail;
    }

    @Override
    public List<PojoCallbackAddress> callbackAddresses() {
        return callbackAddresses != null ? callbackAddresses : List.of();
    }

    @Override
    public long createdAt() {
        return createdAt;
    }
}
