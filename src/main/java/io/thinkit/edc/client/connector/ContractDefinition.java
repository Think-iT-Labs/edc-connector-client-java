package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;

import jakarta.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

public class ContractDefinition {
    private final JsonObject raw;

    public ContractDefinition(JsonObject raw) {
        this.raw = raw;
    }

    public String id() {
        return raw.getString(ID);
    }

    public String accessPolicyId() {
        return raw.getJsonArray(EDC_NAMESPACE + "accessPolicyId")
                .getJsonObject(0)
                .getString(VALUE);
    }

    public String contractPolicyId() {
        return raw.getJsonArray(EDC_NAMESPACE + "contractPolicyId")
                .getJsonObject(0)
                .getString(VALUE);
    }

    public List<Criterion> assetsSelector() {
        return raw.getJsonArray(EDC_NAMESPACE + "assetsSelector").stream()
                .map(s -> new Criterion(s.asJsonObject()))
                .collect(Collectors.toList());
    }

    public long createdAt() {
        return raw.getJsonArray(EDC_NAMESPACE + "createdAt")
                .getJsonObject(0)
                .getJsonNumber(VALUE)
                .longValue();
    }
}
