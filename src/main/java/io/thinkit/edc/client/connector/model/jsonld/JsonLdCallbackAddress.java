package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.CallbackAddress;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.List;

public class JsonLdCallbackAddress extends JsonLdObject implements CallbackAddress {
    private static final String TYPE_CALLBACK_ADDRESS = EDC_NAMESPACE + "CallbackAddress";
    private static final String CALLBACK_ADDRESS_AUTH_CODE_ID = EDC_NAMESPACE + "authCodeId";
    private static final String CALLBACK_ADDRESS_AUTH_KEY = EDC_NAMESPACE + "authKey";
    private static final String CALLBACK_ADDRESS_TRANSACTIONAL = EDC_NAMESPACE + "transactional";
    private static final String CALLBACK_ADDRESS_URI = EDC_NAMESPACE + "uri";
    private static final String CALLBACK_ADDRESS_EVENTS = EDC_NAMESPACE + "events";

    private JsonLdCallbackAddress(JsonObject raw) {
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

    public static class Builder extends AbstractBuilder<JsonLdCallbackAddress, Builder> {

        public static JsonLdCallbackAddress.Builder newInstance() {
            return new JsonLdCallbackAddress.Builder();
        }

        public JsonLdCallbackAddress build() {
            return new JsonLdCallbackAddress(
                    builder.add(TYPE, TYPE_CALLBACK_ADDRESS).build());
        }

        public JsonLdCallbackAddress.Builder authCodeId(String authCodeId) {
            builder.add(
                    CALLBACK_ADDRESS_AUTH_CODE_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, authCodeId)));
            return this;
        }

        public JsonLdCallbackAddress.Builder authKey(String authKey) {
            builder.add(
                    CALLBACK_ADDRESS_AUTH_KEY,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, authKey)));
            return this;
        }

        public JsonLdCallbackAddress.Builder transactional(Boolean transactional) {
            builder.add(
                    CALLBACK_ADDRESS_TRANSACTIONAL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, transactional)));
            return this;
        }

        public JsonLdCallbackAddress.Builder uri(String uri) {
            builder.add(
                    CALLBACK_ADDRESS_URI,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, uri)));
            return this;
        }

        public JsonLdCallbackAddress.Builder events(List<String> events) {
            builder.add(CALLBACK_ADDRESS_EVENTS, Json.createArrayBuilder(events));
            return this;
        }
    }
}
