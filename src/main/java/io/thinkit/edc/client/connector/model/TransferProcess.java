package io.thinkit.edc.client.connector.model;

import io.thinkit.edc.client.connector.model.jsonld.JsonLdProperties;
import java.util.List;

public interface TransferProcess {
    String id();

    String correlationId();

    String type();

    String state();

    Long stateTimestamp();

    String assetId();

    String contractId();

    JsonLdProperties dataDestination();

    JsonLdProperties privateProperties();

    String errorDetail();

    List<? extends CallbackAddress> callbackAddresses();

    long createdAt();
}
