package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

import io.thinkit.edc.client.connector.model.jsonld.JsonLdPolicy;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class PolicyDefinition extends JsonLdObject {
    private static final String POLICY_DEFINITION_POLICY = EDC_NAMESPACE + "policy";
    private static final String POLICY_DEFINITION_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private PolicyDefinition(JsonObject raw) {
        super(raw);
    }

    public JsonLdPolicy policy() {
        return new JsonLdPolicy(object(POLICY_DEFINITION_POLICY));
    }

    public long createdAt() {
        return longValue(POLICY_DEFINITION_CREATED_AT);
    }

    public static class Builder extends AbstractBuilder<PolicyDefinition, PolicyDefinition.Builder> {

        public static PolicyDefinition.Builder newInstance() {
            return new PolicyDefinition.Builder();
        }

        public PolicyDefinition build() {
            return new PolicyDefinition(
                    builder.add(TYPE, EDC_NAMESPACE + "PolicyDefinition").build());
        }

        public PolicyDefinition.Builder policy(JsonLdPolicy policy) {
            builder.add(POLICY_DEFINITION_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }
    }
}
