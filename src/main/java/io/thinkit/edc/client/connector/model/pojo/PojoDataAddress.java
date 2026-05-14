package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.thinkit.edc.client.connector.model.DataAddress;
import io.thinkit.edc.client.connector.model.Properties;
import jakarta.json.JsonObject;
import java.io.IOException;

@JsonDeserialize(using = PojoDataAddress.Deserializer.class)
public class PojoDataAddress implements DataAddress {

    private Properties properties;

    @Override
    public String type() {
        return properties.getString("type");
    }

    @Override
    public Properties properties() {
        return properties;
    }

    @Override
    public JsonObject raw() {
        throw new IllegalCallerException("Not Implemented for pojo");
    }

    public static class Deserializer extends JsonDeserializer<PojoDataAddress> {
        @Override
        public PojoDataAddress deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            var pojo = new PojoDataAddress();

            var builder = PojoProperties.Builder.newInstance();
            parser.getCodec().<JsonNode>readTree(parser).properties().forEach(entry -> {
                builder.property(entry.getKey(), entry.getValue().asText());
            });

            pojo.properties = builder.build();

            return pojo;
        }
    }
}
