package io.thinkit.edc.client.connector.model;

import java.util.List;

public interface Catalog {
    String id();

    List<? extends Dataset> datasets();

    String participantId();

    List<? extends Service> services();
}
