package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.List;

public class ContractRequest extends JsonLdObject {
    private static final String TYPE_CONTRACT_REQUEST = EDC_NAMESPACE + "ContractRequest";
    private static final String CONTRACT_REQUEST_COUNTER_PARTY_ADDRESS = EDC_NAMESPACE + "counterPartyAddress";
    private static final String CONTRACT_REQUEST_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String CONTRACT_REQUEST_PROVIDER_ID = EDC_NAMESPACE + "providerId";
    private static final String CONTRACT_REQUEST_POLICY = EDC_NAMESPACE + "policy";
    private static final String CONTRACT_REQUEST_CALLBACK_ADDRESSES = EDC_NAMESPACE + "callbackAddresses";

    private ContractRequest(JsonObject raw) {
        super(raw);
    }

    public String counterPartyAddress() {
        return stringValue(CONTRACT_REQUEST_COUNTER_PARTY_ADDRESS);
    }

    public String protocol() {
        return stringValue(CONTRACT_REQUEST_PROTOCOL);
    }

    public String providerId() {
        return stringValue(CONTRACT_REQUEST_PROVIDER_ID);
    }

    public Policy policy() {
        return new Policy(object(CONTRACT_REQUEST_POLICY));
    }

    public List<CallbackAddress> callbackAddresses() {
        return objects(CONTRACT_REQUEST_CALLBACK_ADDRESSES)
                .map(it -> CallbackAddress.Builder.newInstance().raw(it).build())
                .toList();
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_CONTRACT_REQUEST);

        public static ContractRequest.Builder newInstance() {
            return new ContractRequest.Builder();
        }

        public ContractRequest build() {
            return new ContractRequest(builder.build());
        }

        public ContractRequest.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public ContractRequest.Builder counterPartyAddress(String counterPartyAddress) {
            builder.add(
                    CONTRACT_REQUEST_COUNTER_PARTY_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyAddress)));
            return this;
        }

        public ContractRequest.Builder protocol(String protocol) {
            builder.add(
                    CONTRACT_REQUEST_PROTOCOL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, protocol)));
            return this;
        }

        public ContractRequest.Builder providerId(String providerId) {
            builder.add(
                    CONTRACT_REQUEST_PROVIDER_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, providerId)));
            return this;
        }

        public ContractRequest.Builder policy(Policy policy) {
            builder.add(CONTRACT_REQUEST_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }

        public ContractRequest.Builder callbackAddresses(List<CallbackAddress> callbackAddresses) {
            builder.add(
                    CONTRACT_REQUEST_CALLBACK_ADDRESSES,
                    callbackAddresses.stream().map(CallbackAddress::raw).collect(toJsonArray()));

            return this;
        }

        public ContractRequest.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
