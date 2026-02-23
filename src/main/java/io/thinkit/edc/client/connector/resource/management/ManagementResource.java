package io.thinkit.edc.client.connector.resource.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.resource.EdcResource;
import io.thinkit.edc.client.connector.utils.JsonLdObject;
import io.thinkit.edc.client.connector.utils.JsonLdUtil;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.io.InputStream;
import java.util.function.Function;

public class ManagementResource extends EdcResource {

    protected final String managementUrl;
    protected final String managementVersion;

    protected ManagementResource(EdcClientContext context) {
        super(context);
        var management = context.urls().management();
        if (management == null) {
            throw new IllegalArgumentException("Cannot instantiate %s client without the management url"
                    .formatted(getClass().getSimpleName()));
        }
        managementUrl = management.baseUrl();
        managementVersion = management.version();
    }

    protected JsonObject compact(JsonLdObject input) {
        if (managementVersion.equals(V3)) {
            return JsonLdUtil.compact(input);
        } else {
            return JsonLdUtil.compact(
                    input,
                    Json.createArrayBuilder()
                            /*
                               json schema requires @context to be an array, but the only way to obtain it is to have
                               at least two entries. This is caused by the compaction.
                               After switching to v4 we can avoid the whole compaction operation and treat the input
                               as a simple json object.
                            */
                            .add("https://w3id.org/edc/connector/management/v2")
                            .add("https://w3id.org/edc/connector/management/v2")
                            .build());
        }
    }

    protected <T> Function<Result<InputStream>, Result<T>> responseDeserializer(
            Function<JsonArray, T> v3Mapper, Function<InputStream, T> v4Mapper) {
        return managementVersion.equals(V3)
                ? result -> result.map(JsonLdUtil::expand).map(v3Mapper)
                : result -> result.map(v4Mapper);
    }
}
