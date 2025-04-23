package io.thinkit.edc.client.connector.utils;

import static io.thinkit.edc.client.connector.utils.Constants.CONTEXT;
import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.VOCAB;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import java.io.InputStream;

public class JsonLdUtil {

    public static JsonArray expand(InputStream body) {
        try {
            var jsonDocument = JsonDocument.of(body);
            return JsonLd.expand(jsonDocument).get();
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject ToJsonObject(InputStream body) {
        try {
            var jsonDocument = JsonDocument.of(body);
            return jsonDocument.getJsonContent().get().asJsonObject();
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated(since = "0.4.0")
    public static JsonObject compact(JsonLdObject input) {
        return compact(
                input,
                Json.createObjectBuilder()
                        .add(CONTEXT, Json.createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                        .build());
    }

    public static JsonObject compact(JsonLdObject input, JsonStructure contextInput) {
        var expanded = JsonDocument.of(input.raw());
        var context = JsonDocument.of(contextInput);
        try {
            return JsonLd.compact(expanded, context).get();
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonArray deserializeToArray(InputStream body) throws JsonLdError {
        var jsonDocument = JsonDocument.of(body);
        var content = jsonDocument.getJsonContent().get();
        return content.asJsonArray();
    }
}
