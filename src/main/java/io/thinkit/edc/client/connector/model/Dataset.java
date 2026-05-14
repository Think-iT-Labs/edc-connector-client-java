package io.thinkit.edc.client.connector.model;

import java.util.List;

public interface Dataset {
    String id();

    String description();

    List<? extends Policy> hasPolicy();

    List<? extends Distribution> distribution();
}
