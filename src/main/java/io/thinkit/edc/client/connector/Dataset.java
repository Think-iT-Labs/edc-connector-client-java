package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.List;

public class Dataset extends JsonLdObject {
    private static final String TYPE_DATASET = DCAT_NAMESPACE + "Dataset";
    private static final String DATASET_DESCRIPTION = EDC_NAMESPACE + "description";
    private static final String DATASET_HAS_POLICY = ODRL_NAMESPACE + "hasPolicy";
    private static final String DATASET_DISTRIBUTION = DCAT_NAMESPACE + "distribution";

    private Dataset(JsonObject raw) {
        super(raw);
    }

    public String description() {
        return stringValue(DATASET_DESCRIPTION);
    }

    public Policy hasPolicy() {
        return Policy.Builder.newInstance().raw(object(DATASET_HAS_POLICY)).build();
    }

    public List<Distribution> distribution() {
        return objects(DATASET_DISTRIBUTION).map(Distribution::new).toList();
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder().add(TYPE, TYPE_DATASET);

        public static Dataset.Builder newInstance() {
            return new Dataset.Builder();
        }

        public Dataset build() {
            return new Dataset(builder.build());
        }

        public Dataset.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public Dataset.Builder description(String description) {
            builder.add(
                    DATASET_DESCRIPTION,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, description)));
            return this;
        }

        public Dataset.Builder hasPolicy(Policy hasPolicy) {
            builder.add(DATASET_HAS_POLICY, Json.createObjectBuilder(hasPolicy.raw()));
            return this;
        }

        public Dataset.Builder distribution(List<Distribution> distribution) {
            builder.add(
                    DATASET_DISTRIBUTION,
                    distribution.stream().map(Distribution::raw).collect(toJsonArray()));
            return this;
        }

        public Dataset.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
