package io.thinkit.edc.client.connector.model;

public interface Distribution {

    String id();

    Service accessService();

    String format();
}
