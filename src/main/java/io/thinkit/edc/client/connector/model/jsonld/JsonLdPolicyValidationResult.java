package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.PolicyValidationResult;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.List;

public class JsonLdPolicyValidationResult extends JsonLdObject implements PolicyValidationResult {
    private static final String TYPE_POLICY_VALIDATION_RESULT = DCT_NAMESPACE + "PolicyValidationResult";

    private static final String POLICY_VALIDATION_RESULT_ISVALID = EDC_NAMESPACE + "isValid";
    private static final String POLICY_VALIDATION_RESULT_ERRORS = EDC_NAMESPACE + "errors";

    public JsonLdPolicyValidationResult(JsonObject raw) {
        super(raw);
    }

    public Boolean isValid() {
        return booleanValue(POLICY_VALIDATION_RESULT_ISVALID);
    }

    public List<String> errors() {
        return objects(POLICY_VALIDATION_RESULT_ERRORS)
                .map(it -> it.getString(VALUE))
                .toList();
    }

    public static class Builder
            extends AbstractBuilder<JsonLdPolicyValidationResult, JsonLdPolicyValidationResult.Builder> {

        public static JsonLdPolicyValidationResult.Builder newInstance() {
            return new JsonLdPolicyValidationResult.Builder();
        }

        public JsonLdPolicyValidationResult build() {
            return new JsonLdPolicyValidationResult(
                    builder.add(TYPE, TYPE_POLICY_VALIDATION_RESULT).build());
        }

        public JsonLdPolicyValidationResult.Builder isValid(Boolean isValid) {
            builder.add(
                    POLICY_VALIDATION_RESULT_ISVALID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, isValid)));
            return this;
        }

        public JsonLdPolicyValidationResult.Builder errors(List<String> errors) {
            builder.add(POLICY_VALIDATION_RESULT_ERRORS, Json.createArrayBuilder(errors));
            return this;
        }
    }
}
