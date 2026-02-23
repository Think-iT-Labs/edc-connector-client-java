package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.Policy;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.List;

public class JsonLdPolicy extends JsonLdObject implements Policy {
    private static final String POLICY_TARGET = EDC_NAMESPACE + "target";
    private static final String POLICY_ASSIGNER = EDC_NAMESPACE + "assigner";

    public JsonLdPolicy(JsonObject raw) {
        super(raw);
    }

    public String target() {
        return stringValue(POLICY_TARGET);
    }

    public String assigner() {
        return stringValue(POLICY_ASSIGNER);
    }

    @Override
    public List<JsonObject> permissions() {
        return List.of();
    }

    @Override
    public List<JsonObject> prohibitions() {
        return List.of();
    }

    @Override
    public List<JsonObject> obligations() {
        return List.of();
    }

    public String getStringValue(String key) {
        return stringValue(key);
    }

    public List<JsonObject> getList(String key) {
        return objects(key).toList();
    }

    public static class Builder extends AbstractBuilder<JsonLdPolicy, JsonLdPolicy.Builder> {

        public static JsonLdPolicy.Builder newInstance() {
            return new JsonLdPolicy.Builder();
        }

        public JsonLdPolicy build() {
            return new JsonLdPolicy(builder.add(TYPE, TYPE_POLICY).build());
        }

        public JsonLdPolicy.Builder target(String target) {
            builder.add(
                    POLICY_TARGET,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, target)));
            return this;
        }

        public JsonLdPolicy.Builder assigner(String assigner) {
            builder.add(
                    POLICY_ASSIGNER,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, assigner)));
            return this;
        }
    }
}
