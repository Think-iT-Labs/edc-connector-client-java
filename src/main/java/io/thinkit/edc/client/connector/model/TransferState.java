package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class TransferState extends JsonLdObject {
    private static final String TYPE_TRANSFER_STATE = EDC_NAMESPACE + "TransferState";
    private static final String TRANSFER_PROCESS_STATE = EDC_NAMESPACE + "state";

    private TransferState(JsonObject raw) {
        super(raw);
    }

    public String state() {
        return stringValue(TRANSFER_PROCESS_STATE);
    }

    public static class Builder extends AbstractBuilder<TransferState, TransferState.Builder> {

        public static TransferState.Builder newInstance() {
            return new TransferState.Builder();
        }

        public TransferState build() {
            return new TransferState(builder.add(TYPE, TYPE_TRANSFER_STATE).build());
        }

        public TransferState.Builder state(String state) {
            builder.add(
                    TRANSFER_PROCESS_STATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, state)));
            return this;
        }
    }
}
