package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.Service;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class JsonLdService extends JsonLdObject implements Service {
    private static final String TYPE_SERVICE = DCAT_NAMESPACE + "DataService";
    private static final String SERVICE_TERMS = DCT_NAMESPACE + "terms";
    private static final String SERVICE_ENDPOINT_URL = DCAT_NAMESPACE + "endpointURL";

    private JsonLdService(JsonObject raw) {
        super(raw);
    }

    public String terms() {
        return stringValue(SERVICE_TERMS);
    }

    public String endpointUrl() {
        return stringValue(SERVICE_ENDPOINT_URL);
    }

    public static class Builder extends AbstractBuilder<JsonLdService, JsonLdService.Builder> {

        public static JsonLdService.Builder newInstance() {
            return new JsonLdService.Builder();
        }

        public JsonLdService build() {
            return new JsonLdService(builder.add(TYPE, TYPE_SERVICE).build());
        }

        public JsonLdService.Builder terms(String terms) {
            builder.add(
                    SERVICE_TERMS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, terms)));
            return this;
        }

        public JsonLdService.Builder endpointUrl(String endpointUrl) {
            builder.add(
                    SERVICE_ENDPOINT_URL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, endpointUrl)));
            return this;
        }
    }
}
