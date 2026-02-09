package io.thinkit.edc.client.connector.model;

import jakarta.json.JsonValue;

public interface Properties {

    int size();

    String getString(String key);

    JsonValue raw();
}
