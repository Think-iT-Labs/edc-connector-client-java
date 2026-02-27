package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.model.Dataset;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
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

    public JsonLdPolicy hasPolicy() {
        return JsonLdPolicy.Builder.newInstance()
                .raw(object(DATASET_HAS_POLICY))
                .build();
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

        public JsonLdDataset.Builder hasPolicy(JsonLdPolicy hasPolicy) {
            builder.add(DATASET_HAS_POLICY, Json.createObjectBuilder(hasPolicy.raw()));
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
