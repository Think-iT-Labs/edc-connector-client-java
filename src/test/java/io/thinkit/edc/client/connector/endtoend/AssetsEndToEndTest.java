package io.thinkit.edc.client.connector.endtoend;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.RealTimeConnectorApiTestBase;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdAsset;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
import io.thinkit.edc.client.connector.services.management.Assets;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedClass
@ValueSource(strings = {V3, V4BETA})
class AssetsEndToEndTest extends RealTimeConnectorApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private Assets assets;

    AssetsEndToEndTest(String managementVersion) {
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
            var id = "assetId-" + UUID.randomUUID();
            var created = assets.create(shouldCreateAnAssetRequest(id));

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
            assertThat(created.getContent()).isEqualTo(id);
        }

        @Test
        void should_get_an_asset() {

            var id = "assetId-" + UUID.randomUUID();
            var created = assets.create(shouldCreateAnAssetRequest(id));

            var asset = assets.get(created.getContent());

            assertThat(asset.isSucceeded()).isTrue();
            assertThat(asset.getContent()).isNotNull();
            assertThat(asset.getContent().id()).isEqualTo(created.getContent());
            assertThat(asset.getContent().dataAddress()).isNotNull().satisfies(dataAddress -> {
                assertThat(dataAddress.properties().getString("type")).isEqualTo("data-address-type");
            });
        }

        @Test
        void should_update_an_asset() {
            var id = "assetId-" + UUID.randomUUID();
            var created = assets.create(shouldCreateAnAssetRequest(id));
            var updated = assets.update(shouldUpdateAnAssetRequest(id));

            var asset = assets.get(created.getContent());

            assertThat(updated.isSucceeded()).isTrue();
            assertThat(asset.isSucceeded()).isTrue();
            assertThat(asset.getContent()).isNotNull();
            assertThat(asset.getContent().dataAddress()).isNotNull().satisfies(dataAddress -> {
                assertThat(dataAddress.properties().getString("type")).isEqualTo("HttpData");
            });
        }

        @Test
        void should_delete_an_asset() {
            var id = "assetId-" + UUID.randomUUID();
            var created = assets.create(shouldCreateAnAssetRequest(id));
            var deleted = assets.delete(created.getContent());
            var asset = assets.get(created.getContent());
            assertThat(deleted.isSucceeded()).isTrue();
            assertThat(asset.isSucceeded()).isFalse();
            assertThat(asset.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.type()).isEqualTo("ObjectNotFound");
            });
        }

        @Test
        void should_get_assets() {
            var id = "assetId-" + UUID.randomUUID();
            var created = assets.create(shouldCreateAnAssetRequest(id));

            var assetList = assets.request(shouldGetAssetsQuery());

            assertThat(assetList.getContent()).anyMatch(asset -> asset.id().equals(created.getContent()));
        }
    }

    private Asset shouldCreateAnAssetRequest(String id) {
        var properties = Map.of("key1", "value1", "key2", "value2");
        var privateProperties = Map.of("private-key", "private-value");
        var dataAddress = Map.of("type", "data-address-type");

        return JsonLdAsset.Builder.newInstance()
                .id(id)
                .properties(properties)
                .privateProperties(privateProperties)
                .dataAddress(dataAddress)
                .build();
    }

    private Asset shouldUpdateAnAssetRequest(String id) {
        var properties = Map.of("name", "description");
        var dataAddress = Map.of("type", "HttpData", "baseUrl", "https://jsonplaceholder.typicode.com/users");

        return JsonLdAsset.Builder.newInstance()
                .id(id)
                .properties(properties)
                .dataAddress(dataAddress)
                .build();
    }

    private QuerySpec shouldGetAssetsQuery() {
        return JsonLdQuerySpec.Builder.newInstance()
                .limit(10)
                .sortOrder("DESC")
                .filterExpression(emptyList())
                .build();
    }
}
