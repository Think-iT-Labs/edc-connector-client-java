package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.TransferState;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class JsonLdTransferState extends JsonLdObject implements TransferState {
    private static final String TYPE_TRANSFER_STATE = EDC_NAMESPACE + "TransferState";
    private static final String TRANSFER_PROCESS_STATE = EDC_NAMESPACE + "state";

    private JsonLdTransferState(JsonObject raw) {
        super(raw);
    }

    public String state() {
        return stringValue(TRANSFER_PROCESS_STATE);
    }

    public static class Builder extends AbstractBuilder<JsonLdTransferState, JsonLdTransferState.Builder> {

        public static JsonLdTransferState.Builder newInstance() {
            return new JsonLdTransferState.Builder();
        }

        public JsonLdTransferState build() {
            return new JsonLdTransferState(
                    builder.add(TYPE, TYPE_TRANSFER_STATE).build());
        }

        public JsonLdTransferState.Builder state(String state) {
            builder.add(
                    TRANSFER_PROCESS_STATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, state)));
            return this;
        }
    }
}
