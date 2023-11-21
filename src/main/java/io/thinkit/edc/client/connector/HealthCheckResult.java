package io.thinkit.edc.client.connector;

import jakarta.json.JsonObject;

public class HealthCheckResult {

    private final JsonObject raw;

    public HealthCheckResult(JsonObject raw) {
        this.raw = raw;
    }

    public String component() {
        return raw.getString("component");
    }

    public boolean isHealthy() {
        return raw.getBoolean("isHealthy");
    }

    public Failure failure() {
        return new Failure(raw.getJsonObject("failure"));
    }
}
