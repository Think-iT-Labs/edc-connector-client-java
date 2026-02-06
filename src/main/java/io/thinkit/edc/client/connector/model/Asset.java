package io.thinkit.edc.client.connector.model;

import io.thinkit.edc.client.connector.model.jsonld.JsonLdProperties;

public interface Asset {

    String id();

    JsonLdProperties properties();

    JsonLdProperties privateProperties();

    DataAddress dataAddress();

    long createdAt();
}
