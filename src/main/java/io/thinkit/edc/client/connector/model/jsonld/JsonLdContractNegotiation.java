package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.model.ContractNegotiation;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.List;

public class JsonLdContractNegotiation extends JsonLdObject implements ContractNegotiation {
    private static final String TYPE_CONTRACT_NEGOTIATION = EDC_NAMESPACE + "ContractNegotiation";
    private static final String CONTRACT_NEGOTIATION_TYPE = EDC_NAMESPACE + "type";
    private static final String CONTRACT_NEGOTIATION_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String CONTRACT_NEGOTIATION_COUNTER_PARTY_ID = EDC_NAMESPACE + "counterPartyId";
    private static final String CONTRACT_NEGOTIATION_COUNTER_PARTY_ADDRESS = EDC_NAMESPACE + "counterPartyAddress";
    private static final String CONTRACT_NEGOTIATION_STATE = EDC_NAMESPACE + "state";
    private static final String CONTRACT_NEGOTIATION_CONTRACT_AGREEMENT_ID = EDC_NAMESPACE + "contractAgreementId";
    private static final String CONTRACT_NEGOTIATION_ERROR_DETAIL = EDC_NAMESPACE + "errorDetail";
    private static final String CONTRACT_NEGOTIATION_CALLBACK_ADDRESSES = EDC_NAMESPACE + "callbackAddresses";
    private static final String CONTRACT_NEGOTIATION_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private JsonLdContractNegotiation(JsonObject raw) {
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

    public List<JsonLdCallbackAddress> callbackAddresses() {
        return objects(CONTRACT_NEGOTIATION_CALLBACK_ADDRESSES)
                .map(it -> JsonLdCallbackAddress.Builder.newInstance().raw(it).build())
                .toList();
    }

    public long createdAt() {
        return longValue(CONTRACT_NEGOTIATION_CREATED_AT);
    }

    public static class Builder extends AbstractBuilder<JsonLdContractNegotiation, JsonLdContractNegotiation.Builder> {

        public static JsonLdContractNegotiation.Builder newInstance() {
            return new JsonLdContractNegotiation.Builder();
        }

        public JsonLdContractNegotiation build() {
            return new JsonLdContractNegotiation(
                    builder.add(TYPE, TYPE_CONTRACT_NEGOTIATION).build());
        }

        public JsonLdContractNegotiation.Builder type(String type) {
            builder.add(
                    CONTRACT_NEGOTIATION_TYPE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, type)));
            return this;
        }

        public JsonLdContractNegotiation.Builder protocol(String protocol) {
            builder.add(
                    CONTRACT_NEGOTIATION_PROTOCOL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, protocol)));
            return this;
        }

        public JsonLdContractNegotiation.Builder counterPartyId(String counterPartyId) {
            builder.add(
                    CONTRACT_NEGOTIATION_COUNTER_PARTY_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyId)));
            return this;
        }

        public JsonLdContractNegotiation.Builder counterPartyAddress(String counterPartyAddress) {
            builder.add(
                    CONTRACT_NEGOTIATION_COUNTER_PARTY_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyAddress)));
            return this;
        }

        public JsonLdContractNegotiation.Builder state(String state) {
            builder.add(
                    CONTRACT_NEGOTIATION_STATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, state)));
            return this;
        }

        public JsonLdContractNegotiation.Builder contractAgreementId(String contractAgreementId) {
            builder.add(
                    CONTRACT_NEGOTIATION_CONTRACT_AGREEMENT_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractAgreementId)));
            return this;
        }

        public JsonLdContractNegotiation.Builder errorDetail(String errorDetail) {
            builder.add(
                    CONTRACT_NEGOTIATION_ERROR_DETAIL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, errorDetail)));
            return this;
        }

        public JsonLdContractNegotiation.Builder callbackAddresses(List<JsonLdCallbackAddress> callbackAddresses) {
            builder.add(
                    CONTRACT_NEGOTIATION_CALLBACK_ADDRESSES,
                    callbackAddresses.stream().map(JsonLdCallbackAddress::raw).collect(toJsonArray()));
            return this;
        }
    }
}
