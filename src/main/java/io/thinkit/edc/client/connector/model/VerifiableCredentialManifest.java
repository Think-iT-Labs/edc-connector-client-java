package io.thinkit.edc.client.connector.model;

import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class VerifiableCredentialManifest {

    private static final String VERIFIABLE_CREDENTIAL_MANIFEST_ID = "id";
    private static final String VERIFIABLE_CREDENTIAL_MANIFEST_ISSUANCE_POLICY = "issuancePolicy";
    private static final String VERIFIABLE_CREDENTIAL_MANIFEST_PARTICIPANT_ID = "participantId";
    private static final String VERIFIABLE_CREDENTIAL_MANIFEST_REISSUANCE_POLICY = "reissuancePolicy";
    private static final String VERIFIABLE_CREDENTIAL_MANIFEST_VERIFIABLE_CREDENTIAL_CONTAINER =
            "verifiableCredentialContainer";
    private final JsonObject raw;

    public VerifiableCredentialManifest(JsonObject raw) {
        this.raw = raw;
    }

    public String id() {
        return raw.getString(VERIFIABLE_CREDENTIAL_MANIFEST_ID);
    }

    public Policy issuancePolicy() {
        return new Policy(raw.getJsonObject(VERIFIABLE_CREDENTIAL_MANIFEST_ISSUANCE_POLICY));
    }

    public String participantId() {
        return raw.getString(VERIFIABLE_CREDENTIAL_MANIFEST_PARTICIPANT_ID);
    }

    public Policy reissuancePolicy() {
        return new Policy(raw.getJsonObject(VERIFIABLE_CREDENTIAL_MANIFEST_REISSUANCE_POLICY));
    }

    public VerifiableCredentialContainer verifiableCredentialContainer() {
        return new VerifiableCredentialContainer(
                raw.getJsonObject(VERIFIABLE_CREDENTIAL_MANIFEST_VERIFIABLE_CREDENTIAL_CONTAINER));
    }

    public JsonObject raw() {
        return raw;
    }

    public static class Builder {
        public final JsonObjectBuilder builder = createObjectBuilder();

        public static Builder newInstance() {
            return new Builder();
        }

        public VerifiableCredentialManifest build() {
            return new VerifiableCredentialManifest(builder.build());
        }

        public Builder id(String id) {
            builder.add(VERIFIABLE_CREDENTIAL_MANIFEST_ID, id);
            return this;
        }

        public Builder issuancePolicy(Policy issuancePolicy) {
            builder.add(VERIFIABLE_CREDENTIAL_MANIFEST_ISSUANCE_POLICY, issuancePolicy.raw());
            return this;
        }

        public Builder participantId(String participantId) {
            builder.add(VERIFIABLE_CREDENTIAL_MANIFEST_PARTICIPANT_ID, participantId);
            return this;
        }

        public Builder reissuancePolicy(Policy reissuancePolicy) {
            builder.add(VERIFIABLE_CREDENTIAL_MANIFEST_REISSUANCE_POLICY, reissuancePolicy.raw());
            return this;
        }

        public Builder verifiableCredentialContainer(VerifiableCredentialContainer verifiableCredentialContainer) {
            builder.add(
                    VERIFIABLE_CREDENTIAL_MANIFEST_VERIFIABLE_CREDENTIAL_CONTAINER,
                    verifiableCredentialContainer.raw());
            return this;
        }
    }
}
