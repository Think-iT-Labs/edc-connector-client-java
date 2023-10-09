package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.VALUE;

import jakarta.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

public class Permission {
    private final JsonObject raw;

    public Permission(JsonObject raw) {
        this.raw = raw;
    }

    public String target() {
        return raw.getJsonArray("target").getJsonObject(0).getString(VALUE);
    }

    public String action() {
        return raw.getJsonArray("action").getJsonObject(0).getString(VALUE);
    }

    public List<Constraint> constraint() {
        return raw.getJsonArray("constraint").stream()
                .map(s -> new Constraint(s.asJsonObject()))
                .collect(Collectors.toList());
    }
}
