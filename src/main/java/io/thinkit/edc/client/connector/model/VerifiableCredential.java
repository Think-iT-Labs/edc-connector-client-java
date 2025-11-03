package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

import jakarta.json.*;
import java.util.List;

public record VerifiableCredential(
        String id,
        List<CredentialStatus> credentialStatus,
        List<CredentialSubject> credentialSubject,
        String description,
        String expirationDate,
        String issuanceDate,
        Issuer issuer,
        String name,
        List<String> type) {}
