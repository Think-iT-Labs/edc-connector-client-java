package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;

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

    List<JsonObject> getList(String key) {
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
