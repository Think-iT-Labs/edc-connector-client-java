package io.thinkit.edc.client.connector;

import java.util.Map;

public record AssetInput(
        String id,
        Map<String, Object> properties,
        Map<String, Object> privateProperties,
        Map<String, Object> dataAddress
) {

}
