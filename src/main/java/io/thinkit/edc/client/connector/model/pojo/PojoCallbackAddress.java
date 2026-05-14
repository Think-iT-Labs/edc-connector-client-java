package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.CallbackAddress;
import java.util.List;

public class PojoCallbackAddress implements CallbackAddress {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("authCodeId")
    private String authCodeId;

    @JsonProperty("authKey")
    private String authKey;

    @JsonProperty("transactional")
    private Boolean transactional;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("events")
    private List<String> events;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String authCodeId() {
        return authCodeId;
    }

    @Override
    public String authKey() {
        return authKey;
    }

    @Override
    public Boolean transactional() {
        return transactional;
    }

    @Override
    public String uri() {
        return uri;
    }

    @Override
    public List<String> events() {
        return events != null ? events : List.of();
    }
}
