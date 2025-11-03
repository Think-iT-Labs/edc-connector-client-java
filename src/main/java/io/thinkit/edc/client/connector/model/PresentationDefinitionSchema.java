package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class PresentationDefinitionSchema extends JsonLdObject {

    private static final String TYPE_PRESENTATION_DEFINIITION_SCHEMA_PRESENTATION =
            EDC_NAMESPACE + "presentationDefinition";

    private static final String PRESENTATION_DEFINIITION_SCHEMA_COMMENT = EDC_NAMESPACE + "comment";

    private static final String PRESENTATION_DEFINIITION = EDC_NAMESPACE + "presentationDefinition";

    public PresentationDefinitionSchema(JsonObject raw) {
        super(raw);
    }

    public String comment() {
        return stringValue(PRESENTATION_DEFINIITION_SCHEMA_COMMENT);
    }

    public JsonObject presentationDefinition() {
        return object(PRESENTATION_DEFINIITION);
    }

    public static class Builder extends AbstractBuilder<PresentationDefinitionSchema, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public PresentationDefinitionSchema build() {
            return new PresentationDefinitionSchema(builder.build());
        }

        public Builder comment(String comment) {
            builder.add(
                    PRESENTATION_DEFINIITION_SCHEMA_COMMENT,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, comment)));
            return this;
        }

        public Builder presentationDefinition(JsonObject presentationDefinition) {
            builder.add(PRESENTATION_DEFINIITION, Json.createObjectBuilder(presentationDefinition));
            return this;
        }
    }
}
