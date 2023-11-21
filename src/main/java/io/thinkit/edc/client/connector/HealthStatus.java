package io.thinkit.edc.client.connector;

import jakarta.json.JsonObject;
import java.util.List;

public class HealthStatus {

    private final JsonObject raw;

    public HealthStatus(JsonObject raw) {
        this.raw = raw;
    }

    public boolean isSystemHealthy() {
        return raw.getBoolean("isSystemHealthy");
    }

    public List<HealthCheckResult> componentResults() {
        return raw.getJsonArray("componentResults").stream()
                .map(elem -> new HealthCheckResult(elem.asJsonObject()))
                .toList();
    }
}
