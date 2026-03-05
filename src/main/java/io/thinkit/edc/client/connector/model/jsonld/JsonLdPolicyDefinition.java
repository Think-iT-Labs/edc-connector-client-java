package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;

import io.thinkit.edc.client.connector.model.PolicyDefinition;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class JsonLdPolicyDefinition extends JsonLdObject implements PolicyDefinition {
    private static final String POLICY_DEFINITION_POLICY = EDC_NAMESPACE + "policy";
    private static final String POLICY_DEFINITION_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private JsonLdPolicyDefinition(JsonObject raw) {
        super(raw);
    }

    public JsonLdPolicy policy() {
        return new JsonLdPolicy(object(POLICY_DEFINITION_POLICY));
    }

    public long createdAt() {
        return longValue(POLICY_DEFINITION_CREATED_AT);
    }

    public static class Builder extends AbstractBuilder<JsonLdPolicyDefinition, JsonLdPolicyDefinition.Builder> {

        public static JsonLdPolicyDefinition.Builder newInstance() {
            return new JsonLdPolicyDefinition.Builder();
        }

        public JsonLdPolicyDefinition build() {
            return new JsonLdPolicyDefinition(
                    builder.add(TYPE, EDC_NAMESPACE + "PolicyDefinition").build());
        }

        public JsonLdPolicyDefinition.Builder policy(JsonLdPolicy policy) {
            builder.add(POLICY_DEFINITION_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }
    }
}
