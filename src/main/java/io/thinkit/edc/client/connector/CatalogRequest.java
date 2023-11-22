package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class CatalogRequest extends JsonLdObject {
    private static final String TYPE_CATALOG_REQUEST = EDC_NAMESPACE + "CatalogRequest";
    private static final String CATALOG_REQUEST_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String CATALOG_REQUEST_COUNTER_PARTY_ADDRESS = EDC_NAMESPACE + "counterPartyAddress";
    private static final String CATALOG_REQUEST_QUERY_SPEC = EDC_NAMESPACE + "querySpec";

    private CatalogRequest(JsonObject raw) {
        super(raw);
    }

    public String protocol() {
        return stringValue(CATALOG_REQUEST_PROTOCOL);
    }

    public String counterPartyAddress() {
        return stringValue(CATALOG_REQUEST_COUNTER_PARTY_ADDRESS);
    }

    public QuerySpec querySpec() {
        return QuerySpec.Builder.newInstance()
                .raw(object(CATALOG_REQUEST_QUERY_SPEC))
                .build();
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_CATALOG_REQUEST);

        public static CatalogRequest.Builder newInstance() {
            return new CatalogRequest.Builder();
        }

        public CatalogRequest build() {
            return new CatalogRequest(builder.build());
        }

        public CatalogRequest.Builder protocol(String protocol) {
            builder.add(
                    CATALOG_REQUEST_PROTOCOL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, protocol)));
            return this;
        }

        public CatalogRequest.Builder counterPartyAddress(String counterPartyAddress) {
            builder.add(
                    CATALOG_REQUEST_COUNTER_PARTY_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyAddress)));
            return this;
        }

        public CatalogRequest.Builder querySpec(QuerySpec querySpec) {
            builder.add(CATALOG_REQUEST_QUERY_SPEC, Json.createObjectBuilder(querySpec.raw()));
            return this;
        }

        public CatalogRequest.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
