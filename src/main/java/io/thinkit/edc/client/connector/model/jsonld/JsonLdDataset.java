package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.model.Dataset;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.List;

public class JsonLdDataset extends JsonLdObject implements Dataset {
    private static final String TYPE_DATASET = DCAT_NAMESPACE + "Dataset";
    private static final String DATASET_DESCRIPTION = EDC_NAMESPACE + "description";
    private static final String DATASET_HAS_POLICY = ODRL_NAMESPACE + "hasPolicy";
    private static final String DATASET_DISTRIBUTION = DCAT_NAMESPACE + "distribution";

    private JsonLdDataset(JsonObject raw) {
        super(raw);
    }

    public String description() {
        return stringValue(DATASET_DESCRIPTION);
    }

    public List<JsonLdPolicy> hasPolicy() {
        return objects(DATASET_HAS_POLICY)
                .map(obj -> JsonLdPolicy.Builder.newInstance().raw(obj).build())
                .toList();
    }

    public List<JsonLdDistribution> distribution() {
        return objects(DATASET_DISTRIBUTION).map(JsonLdDistribution::new).toList();
    }

    public static class Builder extends AbstractBuilder<JsonLdDataset, JsonLdDataset.Builder> {

        public static JsonLdDataset.Builder newInstance() {
            return new JsonLdDataset.Builder();
        }

        public JsonLdDataset build() {
            return new JsonLdDataset(builder.add(TYPE, TYPE_DATASET).build());
        }

        public JsonLdDataset.Builder description(String description) {
            builder.add(
                    DATASET_DESCRIPTION,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, description)));
            return this;
        }

        public JsonLdDataset.Builder hasPolicy(List<JsonLdPolicy> policies) {
            builder.add(
                    DATASET_HAS_POLICY, policies.stream().map(JsonLdPolicy::raw).collect(toJsonArray()));
            return this;
        }

        public JsonLdDataset.Builder distribution(List<JsonLdDistribution> distribution) {
            builder.add(
                    DATASET_DISTRIBUTION,
                    distribution.stream().map(JsonLdDistribution::raw).collect(toJsonArray()));
            return this;
        }
    }
}
