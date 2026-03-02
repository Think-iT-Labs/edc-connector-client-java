package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.Catalog;
import io.thinkit.edc.client.connector.model.Dataset;
import io.thinkit.edc.client.connector.model.Service;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class JsonLdCatalog extends JsonLdObject implements Catalog {
    private static final String TYPE_CATALOG = EDC_NAMESPACE + "Catalog";
    private static final String CATALOG_DATASET = DCAT_NAMESPACE + "dataset";
    private static final String CATALOG_SERVICE = DCAT_NAMESPACE + "service";
    private static final String CATALOG_PARTICIPANT_ID = DSPACE_NAMESPACE + "participantId";

    private JsonLdCatalog(JsonObject raw) {
        super(raw);
    }

    public Dataset dataset() {
        return JsonLdDataset.Builder.newInstance().raw(object(CATALOG_DATASET)).build();
    }

    public Service service() {
        return JsonLdService.Builder.newInstance().raw(object(CATALOG_SERVICE)).build();
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

        public static JsonLdCatalog.Builder newInstance() {
            return new JsonLdCatalog.Builder();
        }

        public JsonLdCatalog build() {
            return new JsonLdCatalog(builder.build());
        }

        public JsonLdCatalog.Builder dataset(JsonLdDataset dataset) {
            builder.add(CATALOG_SERVICE, Json.createObjectBuilder(dataset.raw()));
            return this;
        }

        public JsonLdCatalog.Builder service(JsonLdService service) {
            builder.add(CATALOG_DATASET, Json.createObjectBuilder(service.raw()));
            return this;
        }

        public JsonLdCatalog.Builder participantId(String participantId) {
            builder.add(
                    CATALOG_PARTICIPANT_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, participantId)));
            return this;
        }

        public JsonLdCatalog.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
