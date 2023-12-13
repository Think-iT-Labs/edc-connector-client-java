package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.JsonObject;

public class TerminateNegotiation extends JsonLdObject {
    private static final String TYPE_TERMINATE_NEGOTIATION = EDC_NAMESPACE + "TerminateNegotiation";
    private static final String TERMINATE_NEGOTIATION_REASON = EDC_NAMESPACE + "reason";

    private TerminateNegotiation(JsonObject raw) {
        super(raw);
    }

    public String reason() {
        return stringValue(TERMINATE_NEGOTIATION_REASON);
    }

    public static class Builder extends AbstractBuilder<TerminateNegotiation, TerminateNegotiation.Builder> {

        public static TerminateNegotiation.Builder newInstance() {
            return new TerminateNegotiation.Builder();
        }

        public TerminateNegotiation build() {
            return new TerminateNegotiation(
                    builder.add(TYPE, TYPE_TERMINATE_NEGOTIATION).build());
        }

        public TerminateNegotiation.Builder reason(String reason) {
            builder.add(
                    TERMINATE_NEGOTIATION_REASON,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, reason)));
            return this;
        }
    }
}
