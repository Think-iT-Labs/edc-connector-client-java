package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.TransferProcess;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdProperties;
import java.util.List;
import java.util.Map;

public class PojoTransferProcess implements TransferProcess {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("state")
    private String state;

    @JsonProperty("stateTimestamp")
    private Long stateTimestamp;

    @JsonProperty("assetId")
    private String assetId;

    @JsonProperty("contractId")
    private String contractId;

    @JsonProperty("dataDestination")
    private Map<String, Object> dataDestination;

    @JsonProperty("privateProperties")
    private Map<String, Object> privateProperties;

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
    public String correlationId() {
        return correlationId;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String state() {
        return state;
    }

    @Override
    public Long stateTimestamp() {
        return stateTimestamp;
    }

    @Override
    public String assetId() {
        return assetId;
    }

    @Override
    public String contractId() {
        return contractId;
    }

    @Override
    public JsonLdProperties dataDestination() {
        var builder = JsonLdProperties.Builder.newInstance();
        dataDestination.forEach(builder::property);
        return builder.build();
    }

    @Override
    public JsonLdProperties privateProperties() {
        var builder = JsonLdProperties.Builder.newInstance();
        privateProperties.forEach(builder::property);
        return builder.build();
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
