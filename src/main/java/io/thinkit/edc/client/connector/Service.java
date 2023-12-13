package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.JsonObject;

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

    public static class Builder extends AbstractBuilder<Service, Service.Builder> {

        public static Service.Builder newInstance() {
            return new Service.Builder();
        }

        public Service build() {
            return new Service(builder.add(TYPE, TYPE_SERVICE).build());
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
    }
}
