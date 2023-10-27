package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.List;

public class CallbackAddress extends JsonLdObject {
    private static final String TYPE_CALLBACK_ADDRESS = EDC_NAMESPACE + "CallbackAddress";
    private static final String CALLBACK_ADDRESS_AUTH_CODE_ID = EDC_NAMESPACE + "authCodeId";
    private static final String CALLBACK_ADDRESS_AUTH_KEY = EDC_NAMESPACE + "authKey";
    private static final String CALLBACK_ADDRESS_TRANSACTIONAL = EDC_NAMESPACE + "transactional";
    private static final String CALLBACK_ADDRESS_URI = EDC_NAMESPACE + "uri";
    private static final String CALLBACK_ADDRESS_EVENTS = EDC_NAMESPACE + "events";

    private CallbackAddress(JsonObject raw) {
        super(raw);
    }

    public String authCodeId() {
        return stringValue(CALLBACK_ADDRESS_AUTH_CODE_ID);
    }

    public String authKey() {
        return stringValue(CALLBACK_ADDRESS_AUTH_KEY);
    }

    public Boolean transactional() {
        return booleanValue(CALLBACK_ADDRESS_TRANSACTIONAL);
    }

    public String uri() {
        return stringValue(CALLBACK_ADDRESS_URI);
    }

    public List<String> events() {
        return objects(CALLBACK_ADDRESS_EVENTS).map(it -> it.getString(VALUE)).toList();
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_CALLBACK_ADDRESS);

        public static CallbackAddress.Builder newInstance() {
            return new CallbackAddress.Builder();
        }

        public CallbackAddress build() {
            return new CallbackAddress(builder.build());
        }

        public CallbackAddress.Builder authCodeId(String accessPolicyId) {
            builder.add(
                    CALLBACK_ADDRESS_AUTH_CODE_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, accessPolicyId)));
            return this;
        }

        public CallbackAddress.Builder authKey(String CALLBACKPolicyId) {
            builder.add(
                    CALLBACK_ADDRESS_AUTH_KEY,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, CALLBACKPolicyId)));
            return this;
        }

        public CallbackAddress.Builder transactional(Boolean CALLBACKPolicyId) {
            builder.add(
                    CALLBACK_ADDRESS_TRANSACTIONAL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, CALLBACKPolicyId)));
            return this;
        }

        public CallbackAddress.Builder uri(String CALLBACKPolicyId) {
            builder.add(
                    CALLBACK_ADDRESS_URI,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, CALLBACKPolicyId)));
            return this;
        }

        public CallbackAddress.Builder events(List<String> events) {
            builder.add(CALLBACK_ADDRESS_TRANSACTIONAL, Json.createArrayBuilder(events));
            return this;
        }

        public CallbackAddress.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
