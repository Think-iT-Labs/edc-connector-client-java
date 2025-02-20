package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.List;

public class PresentationQueryMessage extends JsonLdObject {

    private static final String TYPE_PRESENTATION_QUERY = EDC_NAMESPACE + "PresentationQueryMessage";
    private static final String PRESENTATION_QUERY_PRESENTATION_DEFINIITION = EDC_NAMESPACE + "presentationDefinition";

    private static final String PRESENTATION_QUERY_SCOPE = EDC_NAMESPACE + "scope";

    private PresentationQueryMessage(JsonObject raw) {
        super(raw);
    }

    public List<String> scope() {
        return objects(PRESENTATION_QUERY_SCOPE).map(it -> it.getString(VALUE)).toList();
    }

    public PresentationDefinitionSchema presentationDefinition() {
        return new PresentationDefinitionSchema(object(PRESENTATION_QUERY_PRESENTATION_DEFINIITION));
    }

    public static class Builder extends AbstractBuilder<PresentationQueryMessage, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public PresentationQueryMessage build() {
            return new PresentationQueryMessage(
                    builder.add(TYPE, TYPE_PRESENTATION_QUERY).build());
        }

        public Builder scope(List<String> scope) {
            builder.add(PRESENTATION_QUERY_SCOPE, Json.createArrayBuilder(scope));
            ;
            return this;
        }

        public Builder policy(PresentationDefinitionSchema presentationDefinition) {
            builder.add(
                    PRESENTATION_QUERY_PRESENTATION_DEFINIITION,
                    Json.createObjectBuilder(presentationDefinition.raw()));
            return this;
        }
    }
}
