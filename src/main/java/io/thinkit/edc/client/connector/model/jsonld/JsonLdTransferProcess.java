package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.model.TransferProcess;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.List;
import java.util.Map;

public class JsonLdTransferProcess extends JsonLdObject implements TransferProcess {
    private static final String TYPE_TRANSFER_PROCESS = EDC_NAMESPACE + "TransferProcess";
    private static final String TRANSFER_PROCESS_CORRELATION_ID = EDC_NAMESPACE + "correlationId";
    private static final String TRANSFER_PROCESS_TYPE = EDC_NAMESPACE + "type";
    private static final String TRANSFER_PROCESS_STATE = EDC_NAMESPACE + "state";
    private static final String TRANSFER_PROCESS_STATE_TIMESTAMP = EDC_NAMESPACE + "stateTimestamp";
    private static final String TRANSFER_PROCESS_ASSET_ID = EDC_NAMESPACE + "assetId";
    private static final String TRANSFER_PROCESS_CONTRACT_ID = EDC_NAMESPACE + "contractId";
    private static final String TRANSFER_PROCESS_DATA_DESTINATION = EDC_NAMESPACE + "dataDestination";
    private static final String TRANSFER_PROCESS_PRIVATE_PROPERTIES = EDC_NAMESPACE + "privateProperties";
    private static final String TRANSFER_PROCESS_ERROR_DETAIL = EDC_NAMESPACE + "errorDetail";
    private static final String TRANSFER_PROCESS_CALLBACK_ADDRESSES = EDC_NAMESPACE + "callbackAddresses";
    private static final String TRANSFER_PROCESS_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private JsonLdTransferProcess(JsonObject raw) {
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

    public String contractId() {
        return stringValue(TRANSFER_PROCESS_CONTRACT_ID);
    }

    public JsonLdProperties dataDestination() {
        return new JsonLdProperties(object(TRANSFER_PROCESS_DATA_DESTINATION));
    }

    public JsonLdProperties privateProperties() {
        return new JsonLdProperties(object(TRANSFER_PROCESS_PRIVATE_PROPERTIES));
    }

    public String errorDetail() {
        return stringValue(TRANSFER_PROCESS_ERROR_DETAIL);
    }

    public List<JsonLdCallbackAddress> callbackAddresses() {
        return objects(TRANSFER_PROCESS_CALLBACK_ADDRESSES)
                .map(it -> JsonLdCallbackAddress.Builder.newInstance().raw(it).build())
                .toList();
    }

    public long createdAt() {
        return longValue(TRANSFER_PROCESS_CREATED_AT);
    }

    public static class Builder extends AbstractBuilder<JsonLdTransferProcess, JsonLdTransferProcess.Builder> {

        public static JsonLdTransferProcess.Builder newInstance() {
            return new JsonLdTransferProcess.Builder();
        }

        public JsonLdTransferProcess build() {
            return new JsonLdTransferProcess(
                    builder.add(TYPE, TYPE_TRANSFER_PROCESS).build());
        }

        public JsonLdTransferProcess.Builder correlationId(String correlationId) {
            builder.add(
                    TRANSFER_PROCESS_CORRELATION_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, correlationId)));
            return this;
        }

        public JsonLdTransferProcess.Builder type(String type) {
            builder.add(
                    TRANSFER_PROCESS_TYPE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, type)));
            return this;
        }

        public JsonLdTransferProcess.Builder state(String state) {
            builder.add(
                    TRANSFER_PROCESS_STATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, state)));
            return this;
        }

        public JsonLdTransferProcess.Builder stateTimestamp(Long stateTimestamp) {
            builder.add(
                    TRANSFER_PROCESS_STATE_TIMESTAMP,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, stateTimestamp)));
            return this;
        }

        public JsonLdTransferProcess.Builder assetId(String assetId) {
            builder.add(
                    TRANSFER_PROCESS_ASSET_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, assetId)));
            return this;
        }

        public JsonLdTransferProcess.Builder contractId(String contractId) {
            builder.add(
                    TRANSFER_PROCESS_CONTRACT_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractId)));
            return this;
        }

        public JsonLdTransferProcess.Builder dataDestination(Map<String, ?> dataDestination) {
            var propertiesBuilder = JsonLdProperties.Builder.newInstance();
            dataDestination.forEach(propertiesBuilder::property);
            builder.add(
                    TRANSFER_PROCESS_DATA_DESTINATION, propertiesBuilder.build().raw());
            return this;
        }

        public JsonLdTransferProcess.Builder privateProperties(Map<String, ?> properties) {
            var propertiesBuilder = JsonLdProperties.Builder.newInstance();
            properties.forEach(propertiesBuilder::property);
            builder.add(
                    TRANSFER_PROCESS_PRIVATE_PROPERTIES,
                    propertiesBuilder.build().raw());
            return this;
        }

        public JsonLdTransferProcess.Builder errorDetail(String errorDetail) {
            builder.add(
                    TRANSFER_PROCESS_ERROR_DETAIL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, errorDetail)));
            return this;
        }

        public JsonLdTransferProcess.Builder callbackAddresses(List<JsonLdCallbackAddress> callbackAddresses) {
            builder.add(
                    TRANSFER_PROCESS_CALLBACK_ADDRESSES,
                    callbackAddresses.stream().map(JsonLdCallbackAddress::raw).collect(toJsonArray()));
            return this;
        }
    }
}
