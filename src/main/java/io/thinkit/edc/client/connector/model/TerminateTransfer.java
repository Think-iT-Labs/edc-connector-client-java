package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class TerminateTransfer extends JsonLdObject {
    private static final String TYPE_TERMINATE_TRANSFER = EDC_NAMESPACE + "TerminateTransfer";
    private static final String TERMINATE_TRANSFER_REASON = EDC_NAMESPACE + "reason";

    private TerminateTransfer(JsonObject raw) {
        super(raw);
    }

    public String reason() {
        return stringValue(TERMINATE_TRANSFER_REASON);
    }

    public static class Builder extends AbstractBuilder<TerminateTransfer, TerminateTransfer.Builder> {

        public static TerminateTransfer.Builder newInstance() {
            return new TerminateTransfer.Builder();
        }

        public TerminateTransfer build() {
            return new TerminateTransfer(
                    builder.add(TYPE, TYPE_TERMINATE_TRANSFER).build());
        }

        public TerminateTransfer.Builder reason(String reason) {
            builder.add(
                    TERMINATE_TRANSFER_REASON,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, reason)));
            return this;
        }
    }
}
