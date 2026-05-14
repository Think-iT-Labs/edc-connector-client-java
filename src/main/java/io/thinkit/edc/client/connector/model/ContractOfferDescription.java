package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.jsonld.JsonLdPolicy;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ContractOfferDescription extends JsonLdObject {
    private static final String TYPE_CONTRACT_OFFER = EDC_NAMESPACE + "ContractOfferDescription";
    private static final String CONTRACT_OFFER_OFFER_ID = EDC_NAMESPACE + "offerId";
    private static final String CONTRACT_OFFER_ASSET_ID = EDC_NAMESPACE + "assetId";

    private static final String CONTRACT_OFFER_POLICY = EDC_NAMESPACE + "policy";

    private ContractOfferDescription(JsonObject raw) {
        super(raw);
    }

    public String offerId() {
        return stringValue(CONTRACT_OFFER_OFFER_ID);
    }

    public String assetId() {
        return stringValue(CONTRACT_OFFER_ASSET_ID);
    }

    public Policy policy() {
        return new JsonLdPolicy(object(CONTRACT_OFFER_POLICY));
    }

    public static class Builder extends AbstractBuilder<ContractOfferDescription, ContractOfferDescription.Builder> {

        public static ContractOfferDescription.Builder newInstance() {
            return new ContractOfferDescription.Builder();
        }

        public ContractOfferDescription build() {
            return new ContractOfferDescription(
                    builder.add(TYPE, TYPE_CONTRACT_OFFER).build());
        }

        public ContractOfferDescription.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public ContractOfferDescription.Builder offerId(String offerId) {
            builder.add(
                    CONTRACT_OFFER_OFFER_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, offerId)));
            return this;
        }

        public ContractOfferDescription.Builder assetId(String assetId) {
            builder.add(
                    CONTRACT_OFFER_ASSET_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, assetId)));
            return this;
        }

        public ContractOfferDescription.Builder policy(JsonLdPolicy policy) {
            builder.add(CONTRACT_OFFER_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }

        public ContractOfferDescription.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
