package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class CredentialSubject {

    private static final String TYPE_CREDENTIAL_SUBJECT = "CredentialSubject";
    private final JsonObject raw;

    public CredentialSubject(JsonObject raw) {
        this.raw = raw;
    }

    public String id() {
        return raw.getString("id");
    }

    public JsonObject raw() {
        return raw;
    }

    public static class Builder {
        public final JsonObjectBuilder builder = createObjectBuilder();

        public static Builder newInstance() {
            return new Builder();
        }

        public CredentialSubject build() {
            return new CredentialSubject(builder.build());
        }

        public CredentialSubject.Builder id(String id) {
            builder.add("id", id);
            return this;
        }

        public CredentialSubject.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
