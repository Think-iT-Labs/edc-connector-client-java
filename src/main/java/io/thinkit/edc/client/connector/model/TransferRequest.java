package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.List;
import java.util.Map;

public class TransferRequest extends JsonLdObject {
    private static final String TYPE_TRANSFER_REQUEST = EDC_NAMESPACE + "TransferRequest";
    private static final String TRANSFER_REQUEST_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String TRANSFER_REQUEST_COUNTER_PARTY_ADDRESS = EDC_NAMESPACE + "counterPartyAddress";
    private static final String TRANSFER_REQUEST_CONNECTOR_ID = EDC_NAMESPACE + "connectorId";
    private static final String TRANSFER_REQUEST_CONTRACT_ID = EDC_NAMESPACE + "contractId";
    private static final String TRANSFER_REQUEST_ASSET_ID = EDC_NAMESPACE + "assetId";
    private static final String TRANSFER_REQUEST_DATA_DESTINATION = EDC_NAMESPACE + "dataDestination";
    private static final String TRANSFER_REQUEST_PRIVATE_PROPERTIES = EDC_NAMESPACE + "privateProperties";
    private static final String TRANSFER_REQUEST_CALLBACK_ADDRESSES = EDC_NAMESPACE + "callbackAddresses";

    private TransferRequest(JsonObject raw) {
        super(raw);
    }

    public String protocol() {
        return stringValue(TRANSFER_REQUEST_PROTOCOL);
    }

    public String counterPartyAddress() {
        return stringValue(TRANSFER_REQUEST_COUNTER_PARTY_ADDRESS);
    }

    public String connectorId() {
        return stringValue(TRANSFER_REQUEST_CONNECTOR_ID);
    }

    public String contractId() {
        return stringValue(TRANSFER_REQUEST_CONTRACT_ID);
    }

    public String assetId() {
        return stringValue(TRANSFER_REQUEST_ASSET_ID);
    }

    public Properties dataDestination() {
        return new Properties(object(TRANSFER_REQUEST_DATA_DESTINATION));
    }

    public Properties privateProperties() {
        return new Properties(object(TRANSFER_REQUEST_PRIVATE_PROPERTIES));
    }

    public List<CallbackAddress> callbackAddresses() {
        return objects(TRANSFER_REQUEST_CALLBACK_ADDRESSES)
                .map(it -> CallbackAddress.Builder.newInstance().raw(it).build())
                .toList();
    }

    public static class Builder extends AbstractBuilder<TransferRequest, TransferRequest.Builder> {

        public static TransferRequest.Builder newInstance() {
            return new TransferRequest.Builder();
        }

        public TransferRequest build() {
            return new TransferRequest(builder.add(TYPE, TYPE_TRANSFER_REQUEST).build());
        }

        public TransferRequest.Builder protocol(String protocol) {
            builder.add(
                    TRANSFER_REQUEST_PROTOCOL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, protocol)));
            return this;
        }

        public TransferRequest.Builder counterPartyAddress(String counterPartyAddress) {
            builder.add(
                    TRANSFER_REQUEST_COUNTER_PARTY_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyAddress)));
            return this;
        }

        public TransferRequest.Builder connectorId(String connectorId) {
            builder.add(
                    TRANSFER_REQUEST_CONNECTOR_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, connectorId)));
            return this;
        }

        public TransferRequest.Builder contractId(String contractId) {
            builder.add(
                    TRANSFER_REQUEST_CONTRACT_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractId)));
            return this;
        }

        public TransferRequest.Builder assetId(String assetId) {
            builder.add(
                    TRANSFER_REQUEST_ASSET_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, assetId)));
            return this;
        }

        public TransferRequest.Builder dataDestination(Map<String, ?> dataDestination) {
            var propertiesBuilder = Properties.Builder.newInstance();
            dataDestination.forEach(propertiesBuilder::property);
            builder.add(
                    TRANSFER_REQUEST_DATA_DESTINATION, propertiesBuilder.build().raw());
            return this;
        }

        public TransferRequest.Builder privateProperties(Map<String, ?> properties) {
            var propertiesBuilder = Properties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(
                    TRANSFER_REQUEST_PRIVATE_PROPERTIES,
                    propertiesBuilder.build().raw());
            return this;
        }

        public TransferRequest.Builder callbackAddresses(List<CallbackAddress> callbackAddresses) {
            builder.add(
                    TRANSFER_REQUEST_CALLBACK_ADDRESSES,
                    callbackAddresses.stream().map(CallbackAddress::raw).collect(toJsonArray()));
            return this;
        }
    }
}
