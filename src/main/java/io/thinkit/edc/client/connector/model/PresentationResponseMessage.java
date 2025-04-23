package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.List;

public class PresentationResponseMessage extends JsonLdObject {

    private static final String TYPE_PRESENTATION_RESPONSE = EDC_NAMESPACE + "PresentationResponseMessage";
    private static final String PRESENTATION_RESPONSE_PRESENTATION = EDC_NAMESPACE + "presentation";

    private PresentationResponseMessage(JsonObject raw) {
        super(raw);
    }

    public List<JsonObject> presentation() {
        return objects(PRESENTATION_RESPONSE_PRESENTATION).toList();
    }

    public static class Builder extends AbstractBuilder<PresentationResponseMessage, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public PresentationResponseMessage build() {
            return new PresentationResponseMessage(
                    builder.add(TYPE, TYPE_PRESENTATION_RESPONSE).build());
        }

        public Builder presentation(List<JsonObject> presentation) {
            builder.add(
                    PRESENTATION_RESPONSE_PRESENTATION, presentation.stream().collect(toJsonArray()));
            return this;
        }
    }
}
