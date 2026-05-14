package io.thinkit.edc.client.connector.model;

import java.util.List;

public interface PolicyValidationResult {

    String id();

    Boolean isValid();

    List<String> errors();
}
