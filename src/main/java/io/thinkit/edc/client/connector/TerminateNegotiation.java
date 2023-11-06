package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class TerminateNegotiation extends JsonLdObject {
    private static final String TYPE_TERMINATE_NEGOTIATION = EDC_NAMESPACE + "TerminateNegotiation";
    private static final String TERMINATE_NEGOTIATION_REASON = EDC_NAMESPACE + "reason";

    private TerminateNegotiation(JsonObject raw) {
        super(raw);
    }

    public String reason() {
        return stringValue(TERMINATE_NEGOTIATION_REASON);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_TERMINATE_NEGOTIATION);

        public static TerminateNegotiation.Builder newInstance() {
            return new TerminateNegotiation.Builder();
        }

        public TerminateNegotiation build() {
            return new TerminateNegotiation(builder.build());
        }

        public TerminateNegotiation.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public TerminateNegotiation.Builder reason(String reason) {
            builder.add(
                    TERMINATE_NEGOTIATION_REASON,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, reason)));
            return this;
        }

        public TerminateNegotiation.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
