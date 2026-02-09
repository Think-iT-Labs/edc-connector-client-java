package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Catalog extends JsonLdObject {
    private static final String TYPE_CATALOG = EDC_NAMESPACE + "Catalog";
    private static final String CATALOG_DATASET = DCAT_NAMESPACE + "dataset";
    private static final String CATALOG_SERVICE = DCAT_NAMESPACE + "service";
    private static final String CATALOG_PARTICIPANT_ID = DSPACE_NAMESPACE + "participantId";

    private Catalog(JsonObject raw) {
        super(raw);
    }

    public Dataset dataset() {
        return Dataset.Builder.newInstance().raw(object(CATALOG_DATASET)).build();
    }

    public Service service() {
        return Service.Builder.newInstance().raw(object(CATALOG_SERVICE)).build();
    }

    public String participantId() {
        return stringValue(CATALOG_PARTICIPANT_ID);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(
                        CONTEXT,
                        createObjectBuilder()
                                .add(VOCAB, EDC_NAMESPACE)
                                .add(DCT_PREFIX, DCT_NAMESPACE)
                                .add(DCAT_PREFIX, DCAT_NAMESPACE)
                                .add(ODRL_PREFIX, ODRL_NAMESPACE)
                                .add(DSCPACE_PREFIX, DSPACE_NAMESPACE))
                .add(TYPE, TYPE_CATALOG);

        public static Catalog.Builder newInstance() {
            return new Catalog.Builder();
        }

        public Catalog build() {
            return new Catalog(builder.build());
        }

        public Catalog.Builder dataset(Dataset dataset) {
            builder.add(CATALOG_SERVICE, Json.createObjectBuilder(dataset.raw()));
            return this;
        }

        public Catalog.Builder service(Service service) {
            builder.add(CATALOG_DATASET, Json.createObjectBuilder(service.raw()));
            return this;
        }

        public Catalog.Builder participantId(String participantId) {
            builder.add(
                    CATALOG_PARTICIPANT_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, participantId)));
            return this;
        }

        public Catalog.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
