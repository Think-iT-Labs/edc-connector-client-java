package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class Edr extends JsonLdObject {

    private static final String TYPE_EDR = EDC_NAMESPACE + "Edr";
    private static final String EDR_TRANSFER_PROCESS_ID = EDC_NAMESPACE + "transferProcessId";
    private static final String EDR_AGREEMENT_ID = EDC_NAMESPACE + "agreementId";
    private static final String EDR_CONTRACT_NEGOTIATION_ID = EDC_NAMESPACE + "contractNegotiationId";
    private static final String EDR_ASSET_ID = EDC_NAMESPACE + "assetId";
    private static final String EDR_PROVIDER_ID = EDC_NAMESPACE + "providerId";
    private static final String EDR_CREATED_AT = EDC_NAMESPACE + "createdAt";

    private Edr(JsonObject raw) {
        super(raw);
    }

    public String transferProcessId() {
        return stringValue(EDR_TRANSFER_PROCESS_ID);
    }

    public String agreementId() {
        return stringValue(EDR_AGREEMENT_ID);
    }

    public String contractNegotiationId() {
        return stringValue(EDR_CONTRACT_NEGOTIATION_ID);
    }

    public String assetId() {
        return stringValue(EDR_ASSET_ID);
    }

    public String providerId() {
        return stringValue(EDR_PROVIDER_ID);
    }

    public long createdAt() {
        return longValue(EDR_CREATED_AT);
    }

    public static class Builder extends AbstractBuilder<Edr, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public Edr build() {
            return new Edr(builder.add(TYPE, TYPE_EDR).build());
        }

        public Edr.Builder transferProcessId(String transferProcessId) {
            builder.add(
                    EDR_TRANSFER_PROCESS_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, transferProcessId)));
            return this;
        }

        public Edr.Builder agreementId(String agreementId) {
            builder.add(
                    EDR_AGREEMENT_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, agreementId)));
            return this;
        }

        public Edr.Builder contractNegotiationId(String contractNegotiationId) {
            builder.add(
                    EDR_CONTRACT_NEGOTIATION_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, contractNegotiationId)));
            return this;
        }

        public Edr.Builder assetId(String assetId) {
            builder.add(
                    EDR_ASSET_ID, createArrayBuilder().add(createObjectBuilder().add(VALUE, assetId)));
            return this;
        }

        public Edr.Builder providerId(String providerId) {
            builder.add(
                    EDR_PROVIDER_ID,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, providerId)));
            return this;
        }
    }
}
