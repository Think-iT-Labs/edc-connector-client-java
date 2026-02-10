package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.model.ContractDefinition;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.List;

public class JsonLdContractDefinition extends JsonLdObject implements ContractDefinition {
    private static final String TYPE_CONTRACT_DEFINITION = EDC_NAMESPACE + "ContractDefinition";
    private static final String CONTRACT_DEFINITION_ACCESS_POLICY_ID = EDC_NAMESPACE + "accessPolicyId";
    private static final String CONTRACT_DEFINITION_CONTRACT_POLICY_ID = EDC_NAMESPACE + "contractPolicyId";
    private static final String CONTRACT_DEFINITION_ASSETS_SELECTOR = EDC_NAMESPACE + "assetsSelector";
    private static final String CONTRACT_DEFINITION__CREATED_AT = EDC_NAMESPACE + "createdAt";

    private JsonLdContractDefinition(JsonObject raw) {
        super(raw);
    }

    public String accessPolicyId() {
        return stringValue(CONTRACT_DEFINITION_ACCESS_POLICY_ID);
    }

    public String contractPolicyId() {
        return stringValue(CONTRACT_DEFINITION_CONTRACT_POLICY_ID);
    }

    public List<JsonLdCriterion> assetsSelector() {
        return objects(CONTRACT_DEFINITION_ASSETS_SELECTOR)
                .map(it -> JsonLdCriterion.Builder.newInstance().raw(it).build())
                .toList();
    }

    public long createdAt() {
        return longValue(CONTRACT_DEFINITION__CREATED_AT);
    }

    public static class Builder extends AbstractBuilder<JsonLdContractDefinition, JsonLdContractDefinition.Builder> {

        public static JsonLdContractDefinition.Builder newInstance() {
            return new JsonLdContractDefinition.Builder();
        }

        public JsonLdContractDefinition build() {
            return new JsonLdContractDefinition(
                    builder.add(TYPE, TYPE_CONTRACT_DEFINITION).build());
        }

        public JsonLdContractDefinition.Builder accessPolicyId(String accessPolicyId) {
            builder.add(
                    CONTRACT_DEFINITION_ACCESS_POLICY_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, accessPolicyId)));
            return this;
        }

        public JsonLdContractDefinition.Builder contractPolicyId(String contractPolicyId) {
            builder.add(
                    CONTRACT_DEFINITION_CONTRACT_POLICY_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractPolicyId)));
            return this;
        }

        public JsonLdContractDefinition.Builder assetsSelector(List<JsonLdCriterion> criteria) {
            builder.add(
                    CONTRACT_DEFINITION_ASSETS_SELECTOR,
                    criteria.stream().map(JsonLdCriterion::raw).collect(toJsonArray()));
            return this;
        }
    }
}
