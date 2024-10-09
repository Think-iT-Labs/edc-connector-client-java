package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.*;
import java.util.List;

public class VerifiableCredential {

    private static final String TYPE_VERIFIABLE_CREDENTIAL = "VerifiableCredential";
    private static final String VERIFIABLE_CREDENTIAL_STATUS = "credentialStatus";
    private static final String VERIFIABLE_CREDENTIAL_SUBJECT = "credentialSubject";
    private static final String VERIFIABLE_CREDENTIAL_DESCRIPTION = "description";
    private static final String VERIFIABLE_CREDENTIAL_EXPIRATION_DATE = "expirationDate";
    private static final String VERIFIABLE_CREDENTIAL_ISSUANCE_DATE = "issuanceDate";
    private static final String VERIFIABLE_CREDENTIAL_ISSUER = "issuer";
    private static final String VERIFIABLE_CREDENTIAL_NAME = "name";
    private static final String VERIFIABLE_CREDENTIAL_TYPE = "type";
    private final JsonObject raw;

    protected VerifiableCredential(JsonObject raw) {
        this.raw = raw;
    }

    public List<CredentialStatus> credentialStatus() {
        return raw.getJsonArray(VERIFIABLE_CREDENTIAL_STATUS).stream()
                .map(JsonValue::asJsonObject)
                .map(it -> CredentialStatus.Builder.newInstance().raw(it).build())
                .toList();
    }

    public List<CredentialSubject> credentialSubject() {
        return raw.getJsonArray(VERIFIABLE_CREDENTIAL_SUBJECT).stream()
                .map(JsonValue::asJsonObject)
                .map(it -> CredentialSubject.Builder.newInstance().raw(it).build())
                .toList();
    }

    public String description() {
        return raw.getString(VERIFIABLE_CREDENTIAL_DESCRIPTION);
    }

    public String expirationDate() {
        return raw.getString(VERIFIABLE_CREDENTIAL_EXPIRATION_DATE);
    }

    public String issuanceDate() {
        return raw.getString(VERIFIABLE_CREDENTIAL_ISSUANCE_DATE);
    }

    public Issuer issuer() {
        return new Issuer(raw.getJsonObject(VERIFIABLE_CREDENTIAL_ISSUER));
    }

    public String name() {
        return raw.getString(VERIFIABLE_CREDENTIAL_NAME);
    }

    public List<String> type() {
        return raw.getJsonArray(VERIFIABLE_CREDENTIAL_TYPE).stream()
                .map(JsonValue::asJsonObject)
                .map(it -> it.getString(VALUE))
                .toList();
    }

    public JsonObject raw() {
        return raw;
    }

    public static class Builder {
        public final JsonObjectBuilder builder = createObjectBuilder();

        public static Builder newInstance() {
            return new Builder();
        }

        public VerifiableCredential build() {
            return new VerifiableCredential(builder.build());
        }

        public Builder credentialStatus(List<CredentialStatus> credentialStatus) {
            JsonArrayBuilder statusArrayBuilder = Json.createArrayBuilder(
                    credentialStatus.stream().map(CredentialStatus::raw).toList());
            builder.add(VERIFIABLE_CREDENTIAL_STATUS, statusArrayBuilder.build());
            return this;
        }

        public Builder credentialSubject(List<CredentialSubject> credentialSubject) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(
                    credentialSubject.stream().map(CredentialSubject::raw).toList());
            builder.add(VERIFIABLE_CREDENTIAL_SUBJECT, arrayBuilder.build());
            return this;
        }

        public Builder description(String description) {
            builder.add(VERIFIABLE_CREDENTIAL_DESCRIPTION, description);
            return this;
        }

        public Builder expirationDate(String expirationDate) {
            builder.add(VERIFIABLE_CREDENTIAL_EXPIRATION_DATE, expirationDate);
            return this;
        }

        public Builder issuanceDate(String issuanceDate) {
            builder.add(VERIFIABLE_CREDENTIAL_ISSUANCE_DATE, issuanceDate);
            return this;
        }

        public Builder issuer(Issuer issuer) {
            builder.add(VERIFIABLE_CREDENTIAL_ISSUER, issuer.raw());
            return this;
        }

        public Builder name(String name) {
            builder.add(VERIFIABLE_CREDENTIAL_NAME, name);
            return this;
        }

        public Builder type(List<String> type) {
            JsonArrayBuilder typeArrayBuilder =
                    Json.createArrayBuilder(type.stream().map(Json::createValue).toList());
            builder.add(VERIFIABLE_CREDENTIAL_TYPE, typeArrayBuilder.build());
            return this;
        }
    }
}
