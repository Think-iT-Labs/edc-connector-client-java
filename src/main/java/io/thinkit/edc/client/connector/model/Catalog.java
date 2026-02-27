package io.thinkit.edc.client.connector.model;

public interface Catalog {
    String id();

    Dataset dataset();

    String participantId();

    Service service();
}
