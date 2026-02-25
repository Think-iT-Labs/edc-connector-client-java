package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.ContractAgreement;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class JsonLdContractAgreement extends JsonLdObject implements ContractAgreement {
    private static final String TYPE_CONTRACT_AGREEMENT = EDC_NAMESPACE + "ContractAgreement";
    private static final String CONTRACT_AGREEMENT_PROVIDER_ID = EDC_NAMESPACE + "providerId";
    private static final String CONTRACT_AGREEMENT_CONSUMER_ID = EDC_NAMESPACE + "consumerId";
    private static final String CONTRACT_AGREEMENT_ASSET_ID = EDC_NAMESPACE + "assetId";
    private static final String CONTRACT_AGREEMENT_CONTRACT_SIGNIN_DATE = EDC_NAMESPACE + "contractSigningDate";
    private static final String CONTRACT_AGREEMENT_POLICY = EDC_NAMESPACE + "policy";

    private JsonLdContractAgreement(JsonObject raw) {
        super(raw);
    }

    public String providerId() {
        return stringValue(CONTRACT_AGREEMENT_PROVIDER_ID);
    }

    public String consumerId() {
        return stringValue(CONTRACT_AGREEMENT_CONSUMER_ID);
    }

    public String assetId() {
        return stringValue(CONTRACT_AGREEMENT_ASSET_ID);
    }

    public long contractSigningDate() {
        return longValue(CONTRACT_AGREEMENT_CONTRACT_SIGNIN_DATE);
    }

    public JsonLdPolicy policy() {
        return new JsonLdPolicy(object(CONTRACT_AGREEMENT_POLICY));
    }

    public static class Builder extends AbstractBuilder<JsonLdContractAgreement, JsonLdContractAgreement.Builder> {

        public static JsonLdContractAgreement.Builder newInstance() {
            return new JsonLdContractAgreement.Builder();
        }

        public JsonLdContractAgreement build() {
            return new JsonLdContractAgreement(
                    builder.add(TYPE, TYPE_CONTRACT_AGREEMENT).build());
        }

        public JsonLdContractAgreement.Builder providerId(String providerId) {
            builder.add(
                    CONTRACT_AGREEMENT_PROVIDER_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, providerId)));
            return this;
        }

        public JsonLdContractAgreement.Builder consumerId(String consumerId) {
            builder.add(
                    CONTRACT_AGREEMENT_CONSUMER_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, consumerId)));
            return this;
        }

        public JsonLdContractAgreement.Builder assetId(String assetId) {
            builder.add(
                    CONTRACT_AGREEMENT_ASSET_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, assetId)));
            return this;
        }

        public JsonLdContractAgreement.Builder contractSigningDate(Long contractSigningDate) {
            builder.add(
                    CONTRACT_AGREEMENT_CONTRACT_SIGNIN_DATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractSigningDate)));
            return this;
        }

        public JsonLdContractAgreement.Builder policy(JsonLdPolicy policy) {
            builder.add(CONTRACT_AGREEMENT_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }
    }
}
