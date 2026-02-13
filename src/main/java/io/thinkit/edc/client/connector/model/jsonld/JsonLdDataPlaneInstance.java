package io.thinkit.edc.client.connector.model.jsonld;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.List;

public class JsonLdDataPlaneInstance extends JsonLdObject implements DataPlaneInstance {
    private static final String TYPE_DATAPLANE_INSTANCE = EDC_NAMESPACE + "DataPlaneInstance";
    private static final String DATAPLANE_INSTANCE_ALLOWED_DEST_TYPES = EDC_NAMESPACE + "allowedDestTypes";
    private static final String DATAPLANE_INSTANCE_ALLOWED_SOURCE_TYPES = EDC_NAMESPACE + "allowedSourceTypes";
    private static final String DATAPLANE_INSTANCE_ALLOWED_TRANSFER_TYPES = EDC_NAMESPACE + "allowedTransferTypes";
    private static final String DATAPLANE_INSTANCE_LAST_ACTIVE = EDC_NAMESPACE + "lastActive";
    private static final String DATAPLANE_INSTANCE_STATE = EDC_NAMESPACE + "state";
    private static final String DATAPLANE_INSTANCE_STATE_TIMESTAMP = EDC_NAMESPACE + "stateTimestamp";
    private static final String DATAPLANE_INSTANCE_TURN_COUNT = EDC_NAMESPACE + "turnCount";
    private static final String DATAPLANE_INSTANCE_URL = EDC_NAMESPACE + "url";

    private JsonLdDataPlaneInstance(JsonObject raw) {
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

    public List<String> allowedTransferTypes() {
        return objects(DATAPLANE_INSTANCE_ALLOWED_TRANSFER_TYPES)
                .map(it -> it.getString(VALUE))
                .toList();
    }

    public int lastActive() {
        return intValue(DATAPLANE_INSTANCE_LAST_ACTIVE);
    }

    public int turnCount() {
        return intValue(DATAPLANE_INSTANCE_TURN_COUNT);
    }

    public String state() {
        return stringValue(DATAPLANE_INSTANCE_STATE);
    }

    public long stateTimestamp() {
        return longValue(DATAPLANE_INSTANCE_STATE_TIMESTAMP);
    }

    public String url() {
        return stringValue(DATAPLANE_INSTANCE_URL);
    }

    public static class Builder extends AbstractBuilder<JsonLdDataPlaneInstance, JsonLdDataPlaneInstance.Builder> {

        public static JsonLdDataPlaneInstance.Builder newInstance() {
            return new JsonLdDataPlaneInstance.Builder();
        }

        public JsonLdDataPlaneInstance build() {
            return new JsonLdDataPlaneInstance(
                    builder.add(TYPE, TYPE_DATAPLANE_INSTANCE).build());
        }

        public JsonLdDataPlaneInstance.Builder allowedDestTypes(List<String> allowedDestTypes) {
            builder.add(DATAPLANE_INSTANCE_ALLOWED_DEST_TYPES, Json.createArrayBuilder(allowedDestTypes));
            return this;
        }

        public JsonLdDataPlaneInstance.Builder allowedSourceTypes(List<String> allowedSourceTypes) {
            builder.add(DATAPLANE_INSTANCE_ALLOWED_SOURCE_TYPES, Json.createArrayBuilder(allowedSourceTypes));
            return this;
        }

        public JsonLdDataPlaneInstance.Builder allowedTransferTypes(List<String> allowedTransferTypes) {
            builder.add(DATAPLANE_INSTANCE_ALLOWED_TRANSFER_TYPES, Json.createArrayBuilder(allowedTransferTypes));
            return this;
        }

        public JsonLdDataPlaneInstance.Builder lastActive(int lastActive) {
            builder.add(
                    DATAPLANE_INSTANCE_LAST_ACTIVE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, lastActive)));
            return this;
        }

        public JsonLdDataPlaneInstance.Builder turnCount(int turnCount) {
            builder.add(
                    DATAPLANE_INSTANCE_TURN_COUNT,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, turnCount)));
            return this;
        }

        public JsonLdDataPlaneInstance.Builder url(String url) {
            builder.add(
                    DATAPLANE_INSTANCE_URL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, url)));
            return this;
        }

        public JsonLdDataPlaneInstance.Builder state(String state) {
            builder.add(
                    DATAPLANE_INSTANCE_STATE,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, state)));
            return this;
        }

        public JsonLdDataPlaneInstance.Builder state(long stateTimestamp) {
            builder.add(
                    DATAPLANE_INSTANCE_STATE_TIMESTAMP,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, stateTimestamp)));
            return this;
        }
    }
}
