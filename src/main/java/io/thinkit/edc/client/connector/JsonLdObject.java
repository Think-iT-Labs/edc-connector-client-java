package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.util.stream.Stream;

public class JsonLdObject {

    private final JsonObject raw;

    public JsonLdObject(JsonObject raw) {
        this.raw = raw;
    }

    public JsonObject raw() {
        return raw;
    }

    public String id() {
        return raw.getString(ID);
    }

    protected JsonObject object(String key) {
        return raw.getJsonArray(key).getJsonObject(0);
    }

    protected Stream<JsonObject> objects(String key) {
        return raw.getJsonArray(key).stream().map(JsonValue::asJsonObject);
    }

    protected String stringValue(String key) {
        return raw.getJsonArray(key).getJsonObject(0).getString(VALUE);
    }

    protected long longValue(String key) {
        return raw.getJsonArray(key).getJsonObject(0).getJsonNumber(VALUE).longValue();
    }

    protected int intValue(String key) {
        return raw.getJsonArray(key).getJsonObject(0).getJsonNumber(VALUE).intValue();
    }

    protected Boolean booleanValue(String key) {
        return raw.getJsonArray(key).getJsonObject(0).getBoolean(VALUE);
    }

    public abstract static class AbstractBuilder<T extends JsonLdObject, B extends AbstractBuilder<T, B>> {

        public final JsonObjectBuilder builder =
                createObjectBuilder().add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE));
        public Class<T> c;

        public abstract T build();

        public B id(String id) {
            builder.add(ID, id);
            return (B) this;
        }

        public B raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return (B) this;
        }
    }
}
