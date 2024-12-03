package io.thinkit.edc.client.connector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DidDocument(
        @JsonProperty("@context") List<Object> context,
        String id,
        List<String> authentication,
        List<DidService> service,
        List<VerificationMethod> verificationMethod) {}
