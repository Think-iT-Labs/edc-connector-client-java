package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.*;
import java.util.List;

public class Policy extends JsonLdObject {

    public Policy(JsonObject raw) {
        super(raw);
    }

    public String getStringValue(String key) {
        return stringValue(key);
    }

    List<JsonObject> getList(String key) {
        return objects(key).toList();
    }

    public static class Builder {
        private final JsonObjectBuilder raw = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, EDC_NAMESPACE + "Policy");

        public static Policy.Builder newInstance() {
            return new Policy.Builder();
        }

        public Policy build() {
            return new Policy(raw.build());
        }

        public Policy.Builder raw(JsonObject raw) {
            this.raw.addAll(createObjectBuilder(raw));
            return this;
        }
    }
}
