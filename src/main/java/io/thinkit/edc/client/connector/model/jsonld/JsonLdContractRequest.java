package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.model.ContractRequest;
import io.thinkit.edc.client.connector.model.Policy;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.List;

public class JsonLdContractRequest extends JsonLdObject implements ContractRequest {
    private static final String CONTRACT_REQUEST_COUNTER_PARTY_ADDRESS = EDC_NAMESPACE + "counterPartyAddress";
    private static final String CONTRACT_REQUEST_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String CONTRACT_REQUEST_POLICY = EDC_NAMESPACE + "policy";
    private static final String CONTRACT_REQUEST_CALLBACK_ADDRESSES = EDC_NAMESPACE + "callbackAddresses";

    private JsonLdContractRequest(JsonObject raw) {
        super(raw);
    }

    public String counterPartyAddress() {
        return stringValue(CONTRACT_REQUEST_COUNTER_PARTY_ADDRESS);
    }

    public String protocol() {
        return stringValue(CONTRACT_REQUEST_PROTOCOL);
    }

    public Policy policy() {
        return new JsonLdPolicy(object(CONTRACT_REQUEST_POLICY));
    }

    public List<JsonLdCallbackAddress> callbackAddresses() {
        return objects(CONTRACT_REQUEST_CALLBACK_ADDRESSES)
                .map(it -> JsonLdCallbackAddress.Builder.newInstance().raw(it).build())
                .toList();
    }

    public static class Builder extends AbstractBuilder<JsonLdContractRequest, JsonLdContractRequest.Builder> {
        public static JsonLdContractRequest.Builder newInstance() {
            return new JsonLdContractRequest.Builder();
        }
        ;

        public JsonLdContractRequest build() {
            return new JsonLdContractRequest(
                    builder.add(TYPE, TYPE_CONTRACT_REQUEST).build());
        }

        public JsonLdContractRequest.Builder counterPartyAddress(String counterPartyAddress) {
            builder.add(
                    CONTRACT_REQUEST_COUNTER_PARTY_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyAddress)));
            return this;
        }

        public JsonLdContractRequest.Builder protocol(String protocol) {
            builder.add(
                    CONTRACT_REQUEST_PROTOCOL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, protocol)));
            return this;
        }

        public JsonLdContractRequest.Builder policy(JsonLdPolicy policy) {
            builder.add(CONTRACT_REQUEST_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }

        public JsonLdContractRequest.Builder callbackAddresses(List<JsonLdCallbackAddress> callbackAddresses) {
            builder.add(
                    CONTRACT_REQUEST_CALLBACK_ADDRESSES,
                    callbackAddresses.stream().map(JsonLdCallbackAddress::raw).collect(toJsonArray()));

            return this;
        }
    }
}
