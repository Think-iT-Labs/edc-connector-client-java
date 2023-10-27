package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.List;

public class ContractNegotiation extends JsonLdObject {
    private static final String TYPE_CONTRACT_NEGOTIATION = EDC_NAMESPACE + "ContractNegotiation";
    private static final String CONTRACT_NEGOTIATION_TYPE = EDC_NAMESPACE + "type";
    private static final String CONTRACT_NEGOTIATION_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String CONTRACT_NEGOTIATION_COUNTER_PARTY_ID = EDC_NAMESPACE + "counterPartyId";
    private static final String CONTRACT_NEGOTIATION_COUNTER_PARTY_ADDRESS = EDC_NAMESPACE + "counterPartyAddress";
    private static final String CONTRACT_NEGOTIATION_STATE = EDC_NAMESPACE + "state";
    private static final String CONTRACT_NEGOTIATION_CONTRACT_AGREEMENT_ID = EDC_NAMESPACE + "contractAgreementId";
    private static final String CONTRACT_NEGOTIATION_ERROR_DETAIL = EDC_NAMESPACE + "errorDetail";
    private static final String CONTRACT_NEGOTIATION_CALLBACK_ADDRESSES = EDC_NAMESPACE + "callbackAddresses";
    private static final String CONTRACT_NEGOTIATION__CREATED_AT = EDC_NAMESPACE + "createdAt";

    private ContractNegotiation(JsonObject raw) {
        super(raw);
    }

    public String type() {
        return stringValue(CONTRACT_NEGOTIATION_TYPE);
    }

    public String protocol() {
        return stringValue(CONTRACT_NEGOTIATION_PROTOCOL);
    }

    public String counterPartyId() {
        return stringValue(CONTRACT_NEGOTIATION_COUNTER_PARTY_ID);
    }

    public String counterPartyAddress() {
        return stringValue(CONTRACT_NEGOTIATION_COUNTER_PARTY_ADDRESS);
    }

    public String state() {
        return stringValue(CONTRACT_NEGOTIATION_STATE);
    }

    public String contractAgreementId() {
        return stringValue(CONTRACT_NEGOTIATION_CONTRACT_AGREEMENT_ID);
    }

    public String errorDetail() {
        return stringValue(CONTRACT_NEGOTIATION_ERROR_DETAIL);
    }

    public List<CallbackAddress> callbackAddresses() {
        return objects(CONTRACT_NEGOTIATION_CALLBACK_ADDRESSES)
                .map(it -> CallbackAddress.Builder.newInstance().raw(it).build())
                .toList();
    }

    public long createdAt() {
        return longValue(CONTRACT_NEGOTIATION__CREATED_AT);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_CONTRACT_NEGOTIATION);

        public static ContractNegotiation.Builder newInstance() {
            return new ContractNegotiation.Builder();
        }

        public ContractNegotiation build() {
            return new ContractNegotiation(builder.build());
        }

        public ContractNegotiation.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public ContractNegotiation.Builder type(String type) {
            builder.add(
                    CONTRACT_NEGOTIATION_TYPE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, type)));
            return this;
        }

        public ContractNegotiation.Builder protocol(String protocol) {
            builder.add(
                    CONTRACT_NEGOTIATION_PROTOCOL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, protocol)));
            return this;
        }

        public ContractNegotiation.Builder counterPartyId(String counterPartyId) {
            builder.add(
                    CONTRACT_NEGOTIATION_COUNTER_PARTY_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyId)));
            return this;
        }

        public ContractNegotiation.Builder counterPartyAddress(String counterPartyAddress) {
            builder.add(
                    CONTRACT_NEGOTIATION_COUNTER_PARTY_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyAddress)));
            return this;
        }

        public ContractNegotiation.Builder state(String state) {
            builder.add(
                    CONTRACT_NEGOTIATION_STATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, state)));
            return this;
        }

        public ContractNegotiation.Builder contractAgreementId(String contractAgreementId) {
            builder.add(
                    CONTRACT_NEGOTIATION_CONTRACT_AGREEMENT_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractAgreementId)));
            return this;
        }

        public ContractNegotiation.Builder errorDetail(String errorDetail) {
            builder.add(
                    CONTRACT_NEGOTIATION_ERROR_DETAIL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, errorDetail)));
            return this;
        }

        public ContractNegotiation.Builder callbackAddresses(List<CallbackAddress> callbackAddresses) {
            builder.add(
                    CONTRACT_NEGOTIATION_CALLBACK_ADDRESSES,
                    callbackAddresses.stream().map(CallbackAddress::raw).collect(toJsonArray()));
            return this;
        }

        public ContractNegotiation.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
