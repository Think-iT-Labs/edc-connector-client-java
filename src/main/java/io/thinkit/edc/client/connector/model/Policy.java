package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.*;
import java.util.List;

public class Policy extends JsonLdObject {
    private static final String TYPE_POLICY = EDC_NAMESPACE + "Policy";

    public Policy(JsonObject raw) {
        super(raw);
    }

    public String getStringValue(String key) {
        return stringValue(key);
    }

    public List<JsonObject> getList(String key) {
        return objects(key).toList();
    }

    public static class Builder extends AbstractBuilder<Policy, Policy.Builder> {

        public static Policy.Builder newInstance() {
            return new Policy.Builder();
        }

        public Policy build() {
            return new Policy(builder.add(TYPE, TYPE_POLICY).build());
        }
    }
}
