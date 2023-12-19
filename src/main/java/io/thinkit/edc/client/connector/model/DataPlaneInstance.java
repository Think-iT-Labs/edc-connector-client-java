package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.List;

public class DataPlaneInstance extends JsonLdObject {
    private static final String TYPE_DATAPLANE_INSTANCE = EDC_NAMESPACE + "DataPlaneInstance";
    private static final String DATAPLANE_INSTANCE_ALLOWED_DEST_TYPES = EDC_NAMESPACE + "allowedDestTypes";
    private static final String DATAPLANE_INSTANCE_ALLOWED_SOURCE_TYPES = EDC_NAMESPACE + "allowedSourceTypes";
    private static final String DATAPLANE_INSTANCE_LAST_ACTIVE = EDC_NAMESPACE + "lastActive";
    private static final String DATAPLANE_INSTANCE_TURN_COUNT = EDC_NAMESPACE + "turnCount";
    private static final String DATAPLANE_INSTANCE_URL = EDC_NAMESPACE + "url";

    private DataPlaneInstance(JsonObject raw) {
        super(raw);
    }

    public List<String> allowedDestTypes() {
        return objects(DATAPLANE_INSTANCE_ALLOWED_DEST_TYPES)
                .map(it -> it.getString(VALUE))
                .toList();
    }

    public List<String> allowedSourceTypes() {
        return objects(DATAPLANE_INSTANCE_ALLOWED_SOURCE_TYPES)
                .map(it -> it.getString(VALUE))
                .toList();
    }

    public int lastActive() {
        return intValue(DATAPLANE_INSTANCE_LAST_ACTIVE);
    }

    public int turnCount() {
        return intValue(DATAPLANE_INSTANCE_TURN_COUNT);
    }

    public String url() {
        return stringValue(DATAPLANE_INSTANCE_URL);
    }

    public static class Builder extends AbstractBuilder<DataPlaneInstance, DataPlaneInstance.Builder> {

        public static DataPlaneInstance.Builder newInstance() {
            return new DataPlaneInstance.Builder();
        }

        public DataPlaneInstance build() {
            return new DataPlaneInstance(
                    builder.add(TYPE, TYPE_DATAPLANE_INSTANCE).build());
        }

        public DataPlaneInstance.Builder allowedDestTypes(List<String> allowedDestTypes) {
            builder.add(DATAPLANE_INSTANCE_ALLOWED_DEST_TYPES, Json.createArrayBuilder(allowedDestTypes));
            return this;
        }

        public DataPlaneInstance.Builder allowedSourceTypes(List<String> allowedSourceTypes) {
            builder.add(DATAPLANE_INSTANCE_ALLOWED_SOURCE_TYPES, Json.createArrayBuilder(allowedSourceTypes));
            return this;
        }

        public DataPlaneInstance.Builder lastActive(int lastActive) {
            builder.add(
                    DATAPLANE_INSTANCE_LAST_ACTIVE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, lastActive)));
            return this;
        }

        public DataPlaneInstance.Builder turnCount(int turnCount) {
            builder.add(
                    DATAPLANE_INSTANCE_TURN_COUNT,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, turnCount)));
            return this;
        }

        public DataPlaneInstance.Builder url(String url) {
            builder.add(
                    DATAPLANE_INSTANCE_URL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, url)));
            return this;
        }
    }
}
