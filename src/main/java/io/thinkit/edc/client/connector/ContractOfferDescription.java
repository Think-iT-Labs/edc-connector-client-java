package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

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
        return new Policy(object(CONTRACT_OFFER_POLICY));
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_CONTRACT_OFFER);

        public static ContractOfferDescription.Builder newInstance() {
            return new ContractOfferDescription.Builder();
        }

        public ContractOfferDescription build() {
            return new ContractOfferDescription(builder.build());
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

        public ContractOfferDescription.Builder policy(Policy policy) {
            builder.add(CONTRACT_OFFER_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }

        public ContractOfferDescription.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
