package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

import jakarta.json.*;
import java.util.List;

public interface Policy {
    static final String TYPE_POLICY = EDC_NAMESPACE + "Policy";

    String target();

    String assigner();

    List<JsonObject> permissions();

    List<JsonObject> prohibitions();

    List<JsonObject> obligations();
}
