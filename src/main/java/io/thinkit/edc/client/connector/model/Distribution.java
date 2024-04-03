package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;

public class Distribution extends JsonLdObject {
    private static final String DISTRIBUTION_FORMAT = DCT_NAMESPACE + "format";
    private static final String DISTRIBUTION_ACCESS_SERVICE = DCAT_NAMESPACE + "accessService";

    public Distribution(JsonObject raw) {
        super(raw);
    }

    public String accessService() {
        return stringValue(DISTRIBUTION_ACCESS_SERVICE);
    }

    public String format() {
        return object(DISTRIBUTION_FORMAT).getString(ID);
    }
}
