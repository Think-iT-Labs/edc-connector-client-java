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
    private static final String CONTRACT_REQUEST_CONNECTOR_ADDRESS = EDC_NAMESPACE + "connectorAddress";
    private static final String CONTRACT_REQUEST_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String CONTRACT_REQUEST_PROVIDER_ID = EDC_NAMESPACE + "providerId";
    private static final String CONTRACT_REQUEST_OFFER = EDC_NAMESPACE + "offer";
    private static final String CONTRACT_REQUEST_CALLBACK_ADDRESSES = EDC_NAMESPACE + "callbackAddresses";

    private ContractRequest(JsonObject raw) {
        super(raw);
    }

    public String connectorAddress() {
        return stringValue(CONTRACT_REQUEST_CONNECTOR_ADDRESS);
    }

    public String protocol() {
        return stringValue(CONTRACT_REQUEST_PROTOCOL);
    }

    public String providerId() {
        return stringValue(CONTRACT_REQUEST_PROVIDER_ID);
    }

    public String offer() {
        return stringValue(CONTRACT_REQUEST_OFFER);
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

        public ContractRequest.Builder connectorAddress(String connectorAddress) {
            builder.add(
                    CONTRACT_REQUEST_CONNECTOR_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, connectorAddress)));
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

        public ContractRequest.Builder offer(ContractOfferDescription offer) {
            builder.add(CONTRACT_REQUEST_OFFER, Json.createObjectBuilder(offer.raw()));
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
