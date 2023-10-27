package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.ID;
import static io.thinkit.edc.client.connector.Constants.VALUE;

import jakarta.json.JsonObject;
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
}
