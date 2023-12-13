package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import jakarta.json.JsonObject;
import java.util.List;
import java.util.Map;

public class TransferProcess extends JsonLdObject {
    private static final String TYPE_TRANSFER_PROCESS = EDC_NAMESPACE + "TransferProcess";
    private static final String TRANSFER_PROCESS_CORRELATION_ID = EDC_NAMESPACE + "correlationId";
    private static final String TRANSFER_PROCESS_TYPE = EDC_NAMESPACE + "type";
    private static final String TRANSFER_PROCESS_STATE = EDC_NAMESPACE + "state";
    private static final String TRANSFER_PROCESS_STATE_TIMESTAMP = EDC_NAMESPACE + "stateTimestamp";
    private static final String TRANSFER_PROCESS_ASSET_ID = EDC_NAMESPACE + "assetId";
    private static final String TRANSFER_PROCESS_CONNECTOR_ID = EDC_NAMESPACE + "connectorId";
    private static final String TRANSFER_PROCESS_CONTRACT_ID = EDC_NAMESPACE + "contractId";
    private static final String TRANSFER_PROCESS_DATA_DESTINATION = EDC_NAMESPACE + "dataDestination";
    private static final String TRANSFER_PROCESS_PRIVATE_PROPERTIES = EDC_NAMESPACE + "privateProperties";
    private static final String TRANSFER_PROCESS_ERROR_DETAIL = EDC_NAMESPACE + "errorDetail";
    private static final String TRANSFER_PROCESS_CALLBACK_ADDRESSES = EDC_NAMESPACE + "callbackAddresses";
    private static final String TRANSFER_PROCESS_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private TransferProcess(JsonObject raw) {
        super(raw);
    }

    public String correlationId() {
        return stringValue(TRANSFER_PROCESS_CORRELATION_ID);
    }

    public String type() {
        return stringValue(TRANSFER_PROCESS_TYPE);
    }

    public String state() {
        return stringValue(TRANSFER_PROCESS_STATE);
    }

    public Long stateTimestamp() {
        return longValue(TRANSFER_PROCESS_STATE_TIMESTAMP);
    }

    public String assetId() {
        return stringValue(TRANSFER_PROCESS_ASSET_ID);
    }

    public String connectorId() {
        return stringValue(TRANSFER_PROCESS_CONNECTOR_ID);
    }

    public String contractId() {
        return stringValue(TRANSFER_PROCESS_CONTRACT_ID);
    }

    public Properties dataDestination() {
        return new Properties(object(TRANSFER_PROCESS_DATA_DESTINATION));
    }

    public Properties privateProperties() {
        return new Properties(object(TRANSFER_PROCESS_PRIVATE_PROPERTIES));
    }

    public String errorDetail() {
        return stringValue(TRANSFER_PROCESS_ERROR_DETAIL);
    }

    public List<CallbackAddress> callbackAddresses() {
        return objects(TRANSFER_PROCESS_CALLBACK_ADDRESSES)
                .map(it -> CallbackAddress.Builder.newInstance().raw(it).build())
                .toList();
    }

    public long createdAt() {
        return longValue(TRANSFER_PROCESS_CREATED_AT);
    }

    public static class Builder extends AbstractBuilder<TransferProcess, TransferProcess.Builder> {

        public static TransferProcess.Builder newInstance() {
            return new TransferProcess.Builder();
        }

        public TransferProcess build() {
            return new TransferProcess(builder.add(TYPE, TYPE_TRANSFER_PROCESS).build());
        }

        public TransferProcess.Builder correlationId(String correlationId) {
            builder.add(
                    TRANSFER_PROCESS_CORRELATION_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, correlationId)));
            return this;
        }

        public TransferProcess.Builder type(String type) {
            builder.add(
                    TRANSFER_PROCESS_TYPE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, type)));
            return this;
        }

        public TransferProcess.Builder state(String state) {
            builder.add(
                    TRANSFER_PROCESS_STATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, state)));
            return this;
        }

        public TransferProcess.Builder stateTimestamp(Long stateTimestamp) {
            builder.add(
                    TRANSFER_PROCESS_STATE_TIMESTAMP,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, stateTimestamp)));
            return this;
        }

        public TransferProcess.Builder assetId(String assetId) {
            builder.add(
                    TRANSFER_PROCESS_ASSET_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, assetId)));
            return this;
        }

        public TransferProcess.Builder connectorId(String connectorId) {
            builder.add(
                    TRANSFER_PROCESS_CONNECTOR_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, connectorId)));
            return this;
        }

        public TransferProcess.Builder contractId(String contractId) {
            builder.add(
                    TRANSFER_PROCESS_CONTRACT_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractId)));
            return this;
        }

        public TransferProcess.Builder dataDestination(Map<String, ?> dataDestination) {
            var propertiesBuilder = Properties.Builder.newInstance();
            dataDestination.forEach(propertiesBuilder::property);
            builder.add(
                    TRANSFER_PROCESS_DATA_DESTINATION, propertiesBuilder.build().raw());
            return this;
        }

        public TransferProcess.Builder privateProperties(Map<String, ?> properties) {
            var propertiesBuilder = Properties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(
                    TRANSFER_PROCESS_PRIVATE_PROPERTIES,
                    propertiesBuilder.build().raw());
            return this;
        }

        public TransferProcess.Builder errorDetail(String errorDetail) {
            builder.add(
                    TRANSFER_PROCESS_ERROR_DETAIL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, errorDetail)));
            return this;
        }

        public TransferProcess.Builder callbackAddresses(List<CallbackAddress> callbackAddresses) {
            builder.add(
                    TRANSFER_PROCESS_CALLBACK_ADDRESSES,
                    callbackAddresses.stream().map(CallbackAddress::raw).collect(toJsonArray()));
            return this;
        }
    }
}
