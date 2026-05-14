package io.thinkit.edc.client.connector.model;

import java.util.List;

public interface DataPlaneInstance {
    String id();

    List<String> allowedDestTypes();

    List<String> allowedSourceTypes();

    List<String> allowedTransferTypes();

    String state();

    long stateTimestamp();

    int lastActive();

    int turnCount();

    String url();
}
