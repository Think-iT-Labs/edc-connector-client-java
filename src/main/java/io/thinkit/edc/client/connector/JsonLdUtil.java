package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.CONTEXT;
import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.VOCAB;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.io.InputStream;

public class JsonLdUtil {

    public static JsonArray expand(InputStream body) throws JsonLdError {
        var jsonDocument = JsonDocument.of(body);
        return JsonLd.expand(jsonDocument).get();
    }

    public static JsonObject compact(JsonLdObject input) throws JsonLdError {
        var expanded = JsonDocument.of(input.raw());
        var context = JsonDocument.of(Json.createObjectBuilder()
                .add(CONTEXT, Json.createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .build());
        return JsonLd.compact(expanded, context).get();
    }
}