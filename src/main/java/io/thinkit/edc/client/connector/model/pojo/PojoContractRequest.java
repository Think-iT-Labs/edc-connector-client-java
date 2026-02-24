package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.thinkit.edc.client.connector.model.ContractRequest;
import io.thinkit.edc.client.connector.model.Policy;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(builder = PojoContractRequest.Builder.class)
public class PojoContractRequest implements ContractRequest {
    @JsonProperty("@context")
    private List<String> context;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("counterPartyAddress")
    private String counterPartyAddress;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("policy")
    private PojoPolicy policy;

    @JsonProperty("callbackAddresses")
    private List<PojoCallbackAddress> callbackAddresses = new ArrayList<>();

    private PojoContractRequest() {}

    @Override
    public String counterPartyAddress() {
        return counterPartyAddress;
    }

    @Override
    public String protocol() {
        return protocol;
    }

    @Override
    public Policy policy() {
        return policy;
    }

    @Override
    public List<PojoCallbackAddress> callbackAddresses() {
        return callbackAddresses;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final PojoContractRequest request = new PojoContractRequest();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder counterPartyAddress(String counterPartyAddress) {
            request.counterPartyAddress = counterPartyAddress;
            return this;
        }

        public Builder protocol(String protocol) {
            request.protocol = protocol;
            return this;
        }

        public Builder policy(PojoPolicy policy) {
            request.policy = policy;
            return this;
        }

        public Builder callbackAddresses(List<PojoCallbackAddress> callbackAddresses) {
            request.callbackAddresses = callbackAddresses;
            return this;
        }

        public PojoContractRequest build() {
            request.type = TYPE_CONTRACT_REQUEST;
            request.context = List.of(
                    "https://w3id.org/edc/v0.0.1/ns/context.jsonld", "https://w3id.org/edc/connector/management/v2");
            return request;
        }
    }
}
