package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.thinkit.edc.client.connector.model.Policy;
import io.thinkit.edc.client.connector.model.PolicyDefinition;
import java.util.List;

@JsonDeserialize(builder = PojoPolicyDefinition.Builder.class)
public class PojoPolicyDefinition implements PolicyDefinition {
    @JsonProperty("@context")
    private List<String> context;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("@id")
    private String id;

    @JsonProperty("policy")
    private PojoPolicy policy;

    @JsonProperty("createdAt")
    private long createdAt;

    @Override
    public String id() {
        return id;
    }

    @Override
    public Policy policy() {
        return policy;
    }

    @Override
    public long createdAt() {
        return createdAt;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final PojoPolicyDefinition request = new PojoPolicyDefinition();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder policy(PojoPolicy policy) {
            request.policy = policy;
            return this;
        }

        public Builder createdAt(long createdAt) {
            request.createdAt = createdAt;
            return this;
        }

        public PojoPolicyDefinition build() {
            // request.type = TYPE_CONTRACT_REQUEST;
            //  request.context = List.of(
            //         "https://w3id.org/edc/v0.0.1/ns/context.jsonld", "https://w3id.org/edc/connector/management/v2");
            return request;
        }
    }
}
