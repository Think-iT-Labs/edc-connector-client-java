package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

public class PojoServiceDeserializer extends StdDeserializer<PojoService> {

    public PojoServiceDeserializer() {
        super(PojoService.class);
    }

    @Override
    public PojoService deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            ObjectNode node = ctxt.getNodeFactory().objectNode();
            node.put("@id", p.getText());
            return ctxt.readTreeAsValue(node, PojoService.class);
        }
        return ctxt.readValue(p, PojoService.class);
    }
}
