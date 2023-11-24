package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Service extends JsonLdObject {
    private static final String TYPE_SERVICE = DCAT_NAMESPACE + "DataService";
    private static final String SERVICE_TERMS = DCT_NAMESPACE + "terms";
    private static final String SERVICE_ENDPOINT_URL = DCT_NAMESPACE + "endpointUrl";

    private Service(JsonObject raw) {
        super(raw);
    }

    public String terms() {
        return stringValue(SERVICE_TERMS);
    }

    public String endpointUrl() {
        return stringValue(SERVICE_ENDPOINT_URL);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder().add(TYPE, TYPE_SERVICE);

        public static Service.Builder newInstance() {
            return new Service.Builder();
        }

        public Service build() {
            return new Service(builder.build());
        }

        public Service.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public Service.Builder terms(String terms) {
            builder.add(
                    SERVICE_TERMS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, terms)));
            return this;
        }

        public Service.Builder endpointUrl(String endpointUrl) {
            builder.add(
                    SERVICE_ENDPOINT_URL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, endpointUrl)));
            return this;
        }

        public Service.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
