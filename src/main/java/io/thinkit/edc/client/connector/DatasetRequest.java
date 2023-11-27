package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class DatasetRequest extends JsonLdObject {
    private static final String TYPE_DATASET_REQUEST = EDC_NAMESPACE + "DatasetRequest";
    private static final String DATASET_REQUEST_PROTOCOL = EDC_NAMESPACE + "protocol";
    private static final String DATASET_REQUEST_COUNTER_PARTY_ADDRESS = EDC_NAMESPACE + "counterPartyAddress";

    private DatasetRequest(JsonObject raw) {
        super(raw);
    }

    public String protocol() {
        return stringValue(DATASET_REQUEST_PROTOCOL);
    }

    public String counterPartyAddress() {
        return stringValue(DATASET_REQUEST_COUNTER_PARTY_ADDRESS);
    }

    public static class Builder {

        private final JsonObjectBuilder builder = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_DATASET_REQUEST);

        public static DatasetRequest.Builder newInstance() {
            return new DatasetRequest.Builder();
        }

        public DatasetRequest build() {
            return new DatasetRequest(builder.build());
        }

        public DatasetRequest.Builder id(String id) {
            builder.add(ID, id);
            return this;
        }

        public DatasetRequest.Builder protocol(String protocol) {
            builder.add(
                    DATASET_REQUEST_PROTOCOL,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, protocol)));
            return this;
        }

        public DatasetRequest.Builder counterPartyAddress(String counterPartyAddress) {
            builder.add(
                    DATASET_REQUEST_COUNTER_PARTY_ADDRESS,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, counterPartyAddress)));
            return this;
        }

        public DatasetRequest.Builder raw(JsonObject raw) {
            builder.addAll(Json.createObjectBuilder(raw));
            return this;
        }
    }
}
