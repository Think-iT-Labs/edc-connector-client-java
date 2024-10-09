package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class VerifiableCredentialContainer {

    private static final String TYPE_VERIFIABLE_CREDENTIAL_CONTAINER = "VerifiableCredentialContainer";
    private static final String VERIFIABLE_CREDENTIAL_CONTAINER_CREDENTIAL = "credential";
    private static final String VERIFIABLE_CREDENTIAL_CONTAINER_FORMAT = "format";
    private static final String VERIFIABLE_CREDENTIAL_CONTAINER_RAW_VC = "rawVc";
    private final JsonObject raw;

    protected VerifiableCredentialContainer(JsonObject raw) {
        this.raw = raw;
    }

    public VerifiableCredential credential() {
        return new VerifiableCredential(raw.getJsonObject(VERIFIABLE_CREDENTIAL_CONTAINER_CREDENTIAL));
    }

    public String format() {
        return raw.getString(VERIFIABLE_CREDENTIAL_CONTAINER_FORMAT);
    }

    public String rawVc() {
        return raw.getString(VERIFIABLE_CREDENTIAL_CONTAINER_RAW_VC);
    }

    public JsonObject raw() {
        return raw;
    }

    public static class Builder {
        public final JsonObjectBuilder builder = createObjectBuilder();

        public static Builder newInstance() {
            return new Builder();
        }

        public VerifiableCredentialContainer build() {
            return new VerifiableCredentialContainer(builder.build());
        }

        public Builder credential(VerifiableCredential credential) {
            builder.add(VERIFIABLE_CREDENTIAL_CONTAINER_CREDENTIAL, credential.raw());
            return this;
        }

        public Builder format(String format) {
            builder.add(VERIFIABLE_CREDENTIAL_CONTAINER_FORMAT, format);
            return this;
        }

        public Builder rawVc(String rawVc) {
            builder.add(VERIFIABLE_CREDENTIAL_CONTAINER_RAW_VC, rawVc);
            return this;
        }
    }
}
