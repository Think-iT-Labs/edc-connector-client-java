package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

public record VerifiableCredentialContainer(VerifiableCredential credential, String format, String rawVc) {}
