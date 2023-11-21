package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class SelectionRequest extends JsonLdObject {
    private static final String TYPE_SELECTION_REQUEST = EDC_NAMESPACE + "SelectionRequest";
    private static final String SELECTION_REQUEST_DESTINATION = EDC_NAMESPACE + "destination";
    private static final String SELECTION_REQUEST_SOURCE = EDC_NAMESPACE + "source";
    private static final String SELECTION_REQUEST_STRATEGY = EDC_NAMESPACE + "strategy";

    private SelectionRequest(JsonObject raw) {
        super(raw);
    }

    public DataAddress destination() {
        return new DataAddress(object(SELECTION_REQUEST_DESTINATION));
    }

    public DataAddress source() {
        return new DataAddress(object(SELECTION_REQUEST_SOURCE));
    }

    public String strategy() {
        return stringValue(SELECTION_REQUEST_STRATEGY);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_SELECTION_REQUEST);

        public static SelectionRequest.Builder newInstance() {
            return new SelectionRequest.Builder();
        }

        public SelectionRequest build() {
            return new SelectionRequest(builder.build());
        }

        public SelectionRequest.Builder destination(DataAddress destination) {
            builder.add(SELECTION_REQUEST_DESTINATION, Json.createObjectBuilder(destination.raw()));
            return this;
        }

        public SelectionRequest.Builder source(DataAddress source) {
            builder.add(SELECTION_REQUEST_SOURCE, Json.createObjectBuilder(source.raw()));
            return this;
        }

        public SelectionRequest.Builder strategy(String strategy) {
            builder.add(
                    SELECTION_REQUEST_STRATEGY,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, strategy)));
            return this;
        }

        public SelectionRequest.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
