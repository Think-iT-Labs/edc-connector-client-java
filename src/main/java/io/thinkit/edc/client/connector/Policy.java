package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.ODRL_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.VALUE;

import jakarta.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

public class Policy {
    private final JsonObject raw;

    public Policy(JsonObject raw) {
        this.raw = raw;
    }

    public String uid() {
        return raw.getJsonArray("uid").getJsonObject(0).getString(VALUE);
    }

    public List<Permission> permission() {
        return raw.getJsonArray(ODRL_NAMESPACE + "permission").stream()
                .map(s -> new Permission(s.asJsonObject()))
                .collect(Collectors.toList());
    }
}
