package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.CatalogRequest;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class JsonLdCatalogRequest extends JsonLdObject implements CatalogRequest {
    private static final String CATALOG_REQUEST_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String CATALOG_REQUEST_COUNTER_PARTY_ADDRESS = EDC_NAMESPACE + "counterPartyAddress";
    private static final String CATALOG_REQUEST_COUNTER_PARTY_ID = EDC_NAMESPACE + "counterPartyId";
    private static final String CATALOG_REQUEST_QUERY_SPEC = EDC_NAMESPACE + "querySpec";

    private JsonLdCatalogRequest(JsonObject raw) {
        super(raw);
    }

    public String protocol() {
        return stringValue(CATALOG_REQUEST_PROTOCOL);
    }

    public String counterPartyAddress() {
        return stringValue(CATALOG_REQUEST_COUNTER_PARTY_ID);
    }

    public String counterPartyId() {
        return stringValue(CATALOG_REQUEST_COUNTER_PARTY_ADDRESS);
    }

    public QuerySpec querySpec() {
        return JsonLdQuerySpec.Builder.newInstance()
                .raw(object(CATALOG_REQUEST_QUERY_SPEC))
                .build();
    }

    public static class Builder extends AbstractBuilder<JsonLdCatalogRequest, JsonLdCatalogRequest.Builder> {

        public static JsonLdCatalogRequest.Builder newInstance() {
            return new JsonLdCatalogRequest.Builder();
        }

        public JsonLdCatalogRequest build() {
            return new JsonLdCatalogRequest(
                    builder.add(TYPE, TYPE_CATALOG_REQUEST).build());
        }

        public JsonLdCatalogRequest.Builder protocol(String protocol) {
            builder.add(
                    CATALOG_REQUEST_PROTOCOL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, protocol)));
            return this;
        }

        public JsonLdCatalogRequest.Builder counterPartyAddress(String counterPartyAddress) {
            builder.add(
                    CATALOG_REQUEST_COUNTER_PARTY_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyAddress)));
            return this;
        }

        public JsonLdCatalogRequest.Builder querySpec(JsonLdQuerySpec querySpec) {
            builder.add(CATALOG_REQUEST_QUERY_SPEC, Json.createObjectBuilder(querySpec.raw()));
            return this;
        }

        public JsonLdCatalogRequest.Builder counterPartyId(String counterPartyId) {
            builder.add(
                    CATALOG_REQUEST_COUNTER_PARTY_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyId)));
            return this;
        }
    }
}
