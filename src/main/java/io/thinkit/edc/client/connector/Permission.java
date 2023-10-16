package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;

import jakarta.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

public class Permission {
    private final JsonObject raw;

    public Permission(JsonObject raw) {
        this.raw = raw;
    }

    public String target() {
        return raw.getJsonArray(ODRL_NAMESPACE + "target").getJsonObject(0).getString(VALUE);
    }

    public String action() {
        return raw.getJsonArray(ODRL_NAMESPACE + "action").getJsonObject(0).getString(VALUE);
    }

    public List<Constraint> constraint() {
        return raw.getJsonArray(ODRL_NAMESPACE + "constraint").stream()
                .map(s -> new Constraint(s.asJsonObject()))
                .collect(Collectors.toList());
    }
}
