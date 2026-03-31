package io.thinkit.edc.client.connector.model;

import java.util.List;

public interface Catalog {
    String id();

    Dataset dataset();

    List<? extends Dataset> datasets();

    String participantId();

    Service service();
}
