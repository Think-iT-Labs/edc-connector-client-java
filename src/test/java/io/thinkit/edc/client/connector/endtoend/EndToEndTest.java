package io.thinkit.edc.client.connector.endtoend;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.RealTimeConnectorApiTestBase;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdAsset;
import io.thinkit.edc.client.connector.services.management.Assets;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedClass
@ValueSource(strings = {V3, V4BETA})
class EndToEndTest extends RealTimeConnectorApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private Assets assets;

    EndToEndTest(String managementVersion) {
        this.managementVersion = managementVersion;
    }

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(getManagementUrl(), managementVersion)
                .build();
        assets = client.assets();
    }

    @Nested
    class Sync {

        @Test
        void should_create_an_asset() {

            var created = assets.create(shouldCreateAnAssetRequest());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }
    }

    @Nested
    class Async {

        @Test
        void should_create_an_asset_async() {

            var result = assets.createAsync(shouldCreateAnAssetRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }
    }

    private Asset shouldCreateAnAssetRequest() {
        var properties = Map.of("key", Map.of("value", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        var dataAddress = Map.of("type", "data-address-type");

        return JsonLdAsset.Builder.newInstance()
                .id("assetId-" + UUID.randomUUID())
                .properties(properties)
                .privateProperties(privateProperties)
                .dataAddress(dataAddress)
                .build();
    }
}
