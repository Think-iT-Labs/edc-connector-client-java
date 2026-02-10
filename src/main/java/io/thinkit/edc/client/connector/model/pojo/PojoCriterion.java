package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.thinkit.edc.client.connector.model.Criterion;

public class PojoCriterion implements Criterion {

    @JsonProperty("operator")
    private String operator;

    @JsonProperty("operandLeft")
    private Object operandLeft;

    @JsonProperty("operandRight")
    private Object operandRight;

    @Override
    public String operator() {
        return operator;
    }

    @Override
    public Object operandLeft() {
        return operandLeft;
    }

    @Override
    public Object operandRight() {
        return operandRight;
    }
}
