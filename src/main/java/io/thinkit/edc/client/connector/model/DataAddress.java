package io.thinkit.edc.client.connector.model;

import jakarta.json.JsonObject;

public interface DataAddress {

    String type();

    Properties properties();

    JsonObject raw();
}
