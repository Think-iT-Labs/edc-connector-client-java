package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class VerifiableCredentialResource {

    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_CREDENTIAL_STATUS = "credentialStatus";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_HOLDER_ID = "holderId";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_ISSUANCE_POLICY = "issuancePolicy";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_ISSUER_ID = "issuerId";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_PARTICIPANT_ID = "participantId";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_REISSUANCE_POLICY = "reissuancePolicy";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_STATE = "state";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_TIME_OF_LAST_STATUS_UPDATE = "timeOfLastStatusUpdate";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_TIMESTAMP = "timestamp";
    private static final String VERIFIABLE_CREDENTIAL_RESOURCE_VERIFIABLE_CREDENTIAL = "verifiableCredential";
    private final JsonObject raw;

    public VerifiableCredentialResource(JsonObject raw) {
        this.raw = raw;
    }

    public String credentialStatus() {
        return raw.getString(VERIFIABLE_CREDENTIAL_RESOURCE_CREDENTIAL_STATUS);
    }

    public String holderId() {
        return raw.getString(VERIFIABLE_CREDENTIAL_RESOURCE_HOLDER_ID);
    }

    public Policy issuancePolicy() {
        return new Policy(raw.getJsonObject(VERIFIABLE_CREDENTIAL_RESOURCE_ISSUANCE_POLICY));
    }

    public String issuerId() {
        return raw.getString(VERIFIABLE_CREDENTIAL_RESOURCE_ISSUER_ID);
    }

    public String participantId() {
        return raw.getString(VERIFIABLE_CREDENTIAL_RESOURCE_PARTICIPANT_ID);
    }

    public Policy reissuancePolicy() {
        return new Policy(raw.getJsonObject(VERIFIABLE_CREDENTIAL_RESOURCE_REISSUANCE_POLICY));
    }

    public int state() {
        return raw.getInt(VERIFIABLE_CREDENTIAL_RESOURCE_STATE);
    }

    public String timeOfLastStatusUpdate() {
        return raw.getString(VERIFIABLE_CREDENTIAL_RESOURCE_TIME_OF_LAST_STATUS_UPDATE);
    }

    public int timestamp() {
        return raw.getInt(VERIFIABLE_CREDENTIAL_RESOURCE_TIMESTAMP);
    }

    public VerifiableCredentialContainer verifiableCredential() {
        return new VerifiableCredentialContainer(
                raw.getJsonObject(VERIFIABLE_CREDENTIAL_RESOURCE_VERIFIABLE_CREDENTIAL));
    }

    public JsonObject raw() {
        return raw;
    }

    public static class Builder {
        public final JsonObjectBuilder builder = createObjectBuilder();

        public static Builder newInstance() {
            return new Builder();
        }

        public VerifiableCredentialResource build() {
            return new VerifiableCredentialResource(builder.build());
        }

        public Builder credentialStatus(String credentialStatus) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_CREDENTIAL_STATUS, credentialStatus);
            return this;
        }

        public Builder holderId(String holderId) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_HOLDER_ID, holderId);
            return this;
        }

        public Builder issuancePolicy(Policy issuancePolicy) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_ISSUANCE_POLICY, issuancePolicy.raw());
            return this;
        }

        public Builder issuerId(String issuerId) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_ISSUER_ID, issuerId);
            return this;
        }

        public Builder participantId(String participantId) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_PARTICIPANT_ID, participantId);
            return this;
        }

        public Builder reissuancePolicy(Policy reissuancePolicy) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_REISSUANCE_POLICY, reissuancePolicy.raw());
            return this;
        }

        public Builder state(int state) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_STATE, state);
            return this;
        }

        public Builder timeOfLastStatusUpdate(String timeOfLastStatusUpdate) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_TIME_OF_LAST_STATUS_UPDATE, timeOfLastStatusUpdate);
            return this;
        }

        public Builder timestamp(int timestamp) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_TIMESTAMP, timestamp);
            return this;
        }

        public Builder verifiableCredential(VerifiableCredentialContainer verifiableCredential) {
            builder.add(VERIFIABLE_CREDENTIAL_RESOURCE_VERIFIABLE_CREDENTIAL, verifiableCredential.raw());
            return this;
        }
    }
}
