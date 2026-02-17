package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import java.util.List;

public class PojoDataPlaneInstance implements DataPlaneInstance {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("allowedDestTypes")
    private List<String> allowedDestTypes;

    @JsonProperty("allowedSourceTypes")
    private List<String> allowedSourceTypes;

    @JsonProperty("allowedTransferTypes")
    private List<String> allowedTransferTypes;

    @JsonProperty("state")
    private String state;

    @JsonProperty("stateTimestamp")
    private long stateTimestamp;

    @JsonProperty("lastActive")
    private int lastActive;

    @JsonProperty("turnCount")
    private int turnCount;

    @JsonProperty("url")
    private String url;

    @Override
    public String id() {
        return id;
    }

    @Override
    public List<String> allowedDestTypes() {
        return allowedDestTypes;
    }

    @Override
    public List<String> allowedSourceTypes() {
        return allowedSourceTypes;
    }

    @Override
    public List<String> allowedTransferTypes() {
        return allowedTransferTypes;
    }

    @Override
    public String state() {
        return state;
    }

    @Override
    public long stateTimestamp() {
        return stateTimestamp;
    }

    @Override
    public int lastActive() {
        return lastActive;
    }

    @Override
    public int turnCount() {
        return turnCount;
    }

    @Override
    public String url() {
        return url;
    }
}
