package io.thinkit.edc.client.connector;

import jakarta.json.JsonObject;

public class ApiErrorDetail {

    private final JsonObject raw;

    public ApiErrorDetail(JsonObject raw) {
        this.raw = raw;
    }

    public String invalidValue() {
        return raw.getString("invalidValue");
    }

    public String message() {
        return raw.getString("message");
    }

    public String path() {
        return raw.getString("path");
    }

    public String type() {
        return raw.getString("type");
    }
}
