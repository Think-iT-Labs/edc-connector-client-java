package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class PolicyDefinition extends JsonLdObject {
    private static final String POLICY_DEFINITION_POLICY = EDC_NAMESPACE + "policy";
    private static final String POLICY_DEFINITION_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private PolicyDefinition(JsonObject raw) {
        super(raw);
    }

    public Policy policy() {
        return new Policy(object(POLICY_DEFINITION_POLICY));
    }

    public long createdAt() {
        return longValue(POLICY_DEFINITION_CREATED_AT);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, EDC_NAMESPACE + "PolicyDefinition");

        public static PolicyDefinition.Builder newInstance() {
            return new PolicyDefinition.Builder();
        }

        public PolicyDefinition build() {
            return new PolicyDefinition(builder.build());
        }

        public PolicyDefinition.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public PolicyDefinition.Builder policy(Policy policy) {
            builder.add(POLICY_DEFINITION_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }

        public PolicyDefinition.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
