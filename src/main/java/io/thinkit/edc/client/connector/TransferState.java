package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class TransferState extends JsonLdObject {
    private static final String TYPE_TRANSFER_STATE = EDC_NAMESPACE + "TransferState";
    private static final String TRANSFER_PROCESS_STATE = EDC_NAMESPACE + "state";

    private TransferState(JsonObject raw) {
        super(raw);
    }

    public String state() {
        return stringValue(TRANSFER_PROCESS_STATE);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_TRANSFER_STATE);

        public static TransferState.Builder newInstance() {
            return new TransferState.Builder();
        }

        public TransferState build() {
            return new TransferState(builder.build());
        }

        public TransferState.Builder state(String state) {
            builder.add(
                    TRANSFER_PROCESS_STATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, state)));
            return this;
        }

        public TransferState.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
