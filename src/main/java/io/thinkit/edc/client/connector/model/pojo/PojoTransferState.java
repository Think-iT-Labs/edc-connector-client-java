package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.TransferState;

public class PojoTransferState implements TransferState {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("state")
    private String state;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String state() {
        return state;
    }
}
