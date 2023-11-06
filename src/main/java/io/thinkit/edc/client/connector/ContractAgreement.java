package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class ContractAgreement extends JsonLdObject {
    private static final String TYPE_CONTRACT_AGREEMENT = EDC_NAMESPACE + "ContractAgreement";
    private static final String CONTRACT_AGREEMENT_PROVIDER_ID = EDC_NAMESPACE + "providerId";
    private static final String CONTRACT_AGREEMENT_CONSUMER_ID = EDC_NAMESPACE + "consumerId";
    private static final String CONTRACT_AGREEMENT_ASSET_ID = EDC_NAMESPACE + "assetId";
    private static final String CONTRACT_AGREEMENT_CONTRACT_SIGNIN_DATE = EDC_NAMESPACE + "contractSigningDate";
    private static final String CONTRACT_AGREEMENT_POLICY = EDC_NAMESPACE + "policy";

    private ContractAgreement(JsonObject raw) {
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

    public Policy policy() {
        return new Policy(object(CONTRACT_AGREEMENT_POLICY));
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_CONTRACT_AGREEMENT);

        public static ContractAgreement.Builder newInstance() {
            return new ContractAgreement.Builder();
        }

        public ContractAgreement build() {
            return new ContractAgreement(builder.build());
        }

        public ContractAgreement.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public ContractAgreement.Builder providerId(String providerId) {
            builder.add(
                    CONTRACT_AGREEMENT_PROVIDER_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, providerId)));
            return this;
        }

        public ContractAgreement.Builder consumerId(String consumerId) {
            builder.add(
                    CONTRACT_AGREEMENT_CONSUMER_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, consumerId)));
            return this;
        }

        public ContractAgreement.Builder assetId(String assetId) {
            builder.add(
                    CONTRACT_AGREEMENT_ASSET_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, assetId)));
            return this;
        }

        public ContractAgreement.Builder contractSigningDate(Long contractSigningDate) {
            builder.add(
                    CONTRACT_AGREEMENT_CONTRACT_SIGNIN_DATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractSigningDate)));
            return this;
        }

        public ContractAgreement.Builder policy(Policy policy) {
            builder.add(CONTRACT_AGREEMENT_POLICY, Json.createObjectBuilder(policy.raw()));
            return this;
        }

        public ContractAgreement.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
