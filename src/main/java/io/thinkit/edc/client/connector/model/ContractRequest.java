package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;
import static io.thinkit.edc.client.connector.utils.Constants.VALUE;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.model.jsonld.JsonLdCallbackAddress;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
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

    public Policy policy() {
        return new Policy(object(CONTRACT_REQUEST_POLICY));
    }

    public List<JsonLdCallbackAddress> callbackAddresses() {
        return objects(CONTRACT_REQUEST_CALLBACK_ADDRESSES)
                .map(it -> JsonLdCallbackAddress.Builder.newInstance().raw(it).build())
                .toList();
    }

    public static class Builder extends AbstractBuilder<ContractRequest, ContractRequest.Builder> {
        public static ContractRequest.Builder newInstance() {
            return new ContractRequest.Builder();
        }
        ;

        public ContractRequest build() {
            return new ContractRequest(builder.add(TYPE, TYPE_CONTRACT_REQUEST).build());
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

        public ContractRequest.Builder policy(Policy policy) {
            builder.add(CONTRACT_REQUEST_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }

        public ContractRequest.Builder callbackAddresses(List<JsonLdCallbackAddress> callbackAddresses) {
            builder.add(
                    CONTRACT_REQUEST_CALLBACK_ADDRESSES,
                    callbackAddresses.stream().map(JsonLdCallbackAddress::raw).collect(toJsonArray()));

            return this;
        }
    }
}
