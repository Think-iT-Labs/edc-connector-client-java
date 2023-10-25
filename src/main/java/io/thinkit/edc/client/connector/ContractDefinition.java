package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.List;

public class ContractDefinition extends JsonLdObject {
    private static final String TYPE_CONTRACT_DEFINITION = EDC_NAMESPACE + "ContractDefinition";
    private static final String CONTRACT_DEFINITION_ACCESS_POLICY_ID = EDC_NAMESPACE + "accessPolicyId";
    private static final String CONTRACT_DEFINITION_CONTRACT_POLICY_ID = EDC_NAMESPACE + "contractPolicyId";
    private static final String CONTRACT_DEFINITION_ASSETS_SELECTOR = EDC_NAMESPACE + "assetsSelector";
    private static final String CONTRACT_DEFINITION__CREATED_AT = EDC_NAMESPACE + "createdAt";

    private ContractDefinition(JsonObject raw) {
        super(raw);
    }

    public String accessPolicyId() {
        return stringValue(CONTRACT_DEFINITION_ACCESS_POLICY_ID);
    }

    public String contractPolicyId() {
        return stringValue(CONTRACT_DEFINITION_CONTRACT_POLICY_ID);
    }

    public List<Criterion> assetsSelector() {
        return objects(CONTRACT_DEFINITION_ASSETS_SELECTOR)
                .map(it -> Criterion.Builder.newInstance().raw(it).build())
                .toList();
    }

    public long createdAt() {
        return longValue(CONTRACT_DEFINITION__CREATED_AT);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_CONTRACT_DEFINITION);

        public static ContractDefinition.Builder newInstance() {
            return new ContractDefinition.Builder();
        }

        public ContractDefinition build() {
            return new ContractDefinition(builder.build());
        }

        public ContractDefinition.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public ContractDefinition.Builder accessPolicyId(String accessPolicyId) {
            builder.add(
                    CONTRACT_DEFINITION_ACCESS_POLICY_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, accessPolicyId)));
            return this;
        }

        public ContractDefinition.Builder contractPolicyId(String contractPolicyId) {
            builder.add(
                    CONTRACT_DEFINITION_CONTRACT_POLICY_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractPolicyId)));
            return this;
        }

        public ContractDefinition.Builder assetsSelector(List<Criterion> criteria) {
            builder.add(
                    CONTRACT_DEFINITION_ASSETS_SELECTOR,
                    criteria.stream().map(Criterion::raw).collect(toJsonArray()));
            return this;
        }

        public ContractDefinition.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
