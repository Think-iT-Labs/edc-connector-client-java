package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class TerminateTransfer extends JsonLdObject {
    private static final String TYPE_TERMINATE_TRANSFER = EDC_NAMESPACE + "TerminateTransfer";
    private static final String TERMINATE_TRANSFER_REASON = EDC_NAMESPACE + "reason";

    private TerminateTransfer(JsonObject raw) {
        super(raw);
    }

    public String reason() {
        return stringValue(TERMINATE_TRANSFER_REASON);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_TERMINATE_TRANSFER);

        public static TerminateTransfer.Builder newInstance() {
            return new TerminateTransfer.Builder();
        }

        public TerminateTransfer build() {
            return new TerminateTransfer(builder.build());
        }

        public TerminateTransfer.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public TerminateTransfer.Builder reason(String reason) {
            builder.add(
                    TERMINATE_TRANSFER_REASON,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, reason)));
            return this;
        }

        public TerminateTransfer.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
