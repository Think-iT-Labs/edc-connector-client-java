package io.thinkit.edc.client.connector.model;

import java.util.List;

public interface CallbackAddress {
    String id();

    String authCodeId();

    String authKey();

    Boolean transactional();

    String uri();

    List<String> events();
}
