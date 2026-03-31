package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.thinkit.edc.client.connector.model.CatalogRequest;
import io.thinkit.edc.client.connector.model.QuerySpec;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PojoCatalogRequest implements CatalogRequest {
    @JsonProperty("@context")
    private List<String> context;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("@id")
    private String id;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("counterPartyAddress")
    private String counterPartyAddress;

    @JsonProperty("counterPartyId")
    private String counterPartyId;

    @JsonProperty("querySpec")
    private PojoQuerySpec querySpec;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String protocol() {
        return protocol;
    }

    @Override
    public String counterPartyAddress() {
        return counterPartyAddress;
    }

    @Override
    public String counterPartyId() {
        return counterPartyId;
    }

    @Override
    public QuerySpec querySpec() {
        return querySpec;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final PojoCatalogRequest request = new PojoCatalogRequest();

        public static PojoCatalogRequest.Builder newInstance() {
            return new PojoCatalogRequest.Builder();
        }

        public PojoCatalogRequest.Builder id(String id) {
            request.id = id;
            return this;
        }

        public PojoCatalogRequest.Builder protocol(String protocol) {
            request.protocol = protocol;
            return this;
        }

        public PojoCatalogRequest.Builder counterPartyAddress(String counterPartyAddress) {
            request.counterPartyAddress = counterPartyAddress;
            return this;
        }

        public PojoCatalogRequest.Builder counterPartyId(String counterPartyId) {
            request.counterPartyId = counterPartyId;
            return this;
        }

        public PojoCatalogRequest.Builder querySpec(PojoQuerySpec querySpec) {
            request.querySpec = querySpec;
            return this;
        }

        public PojoCatalogRequest build() {
            request.type = "CatalogRequest";
            request.context = List.of(
                    "https://w3id.org/edc/connector/management/v2", "https://w3id.org/edc/connector/management/v2");
            return request;
        }
    }
}
