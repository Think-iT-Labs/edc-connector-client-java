package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.PolicyValidationResult;
import java.util.List;

public class PojoPolicyValidationResult implements PolicyValidationResult {

    @JsonProperty("@id")
    private String id;

    @JsonProperty("isValid")
    private Boolean isValid;

    @JsonProperty("errors")
    private List<String> errors;

    @Override
    public String id() {
        return id;
    }

    @Override
    public Boolean isValid() {
        return isValid;
    }

    @Override
    public List<String> errors() {
        return errors;
    }
}
