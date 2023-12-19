package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.CONTEXT;
import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;
import static io.thinkit.edc.client.connector.utils.Constants.VOCAB;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Criterion extends JsonLdObject {

    private static final String CRITERION_OPERATOR = EDC_NAMESPACE + "operator";
    private static final String CRITERION_OPERAND_LEFT = EDC_NAMESPACE + "operandLeft";
    private static final String CRITERION_OPERAND_RIGHT = EDC_NAMESPACE + "operandRight";

    private Criterion(JsonObject raw) {
        super(raw);
    }

    public String operator() {
        return stringValue(CRITERION_OPERATOR);
    }

    public Object operandLeft() {
        return stringValue(CRITERION_OPERAND_LEFT);
    }

    public Object operandRight() {
        return stringValue(CRITERION_OPERAND_RIGHT);
    }

    public static class Builder {
        private final JsonObjectBuilder raw = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, EDC_NAMESPACE + "Criterion");

        public static Builder newInstance() {
            return new Builder();
        }

        public Criterion build() {
            return new Criterion(raw.build());
        }

        public Builder raw(JsonObject raw) {
            this.raw.addAll(createObjectBuilder(raw));
            return this;
        }
    }
}
