package io.thinkit.edc.client.connector.endtoend;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.RealTimeConnectorApiTestBase;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdAsset;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
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

            var created = assets.create(shouldCreateAnAssetRequest());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_get_an_asset() {

            var created = assets.create(shouldCreateAnAssetRequest());

            var asset = assets.get(created.getContent());

            assertThat(asset.isSucceeded()).isTrue();
            assertThat(asset.getContent()).isNotNull();
            assertThat(asset.getContent().id()).isEqualTo(created.getContent());
        }

        @Test
        void should_update_an_asset() {
            var created = assets.create(shouldCreateAnAssetRequest());
            var updated = assets.update(shouldUpdateAnAssetRequest(created.getContent()));

            assertThat(updated.isSucceeded()).isTrue();
        }

        @Test
        void should_delete_an_asset() {
            var created = assets.create(shouldCreateAnAssetRequest());
            var deleted = assets.delete(created.getContent());

            assertThat(deleted.isSucceeded()).isTrue();
        }

        @Test
        void should_get_assets() {
            var created = assets.create(shouldCreateAnAssetRequest());

            var assetList = assets.request(shouldGetAssetsQuery());

            assertThat(assetList.getContent()).anyMatch(asset -> asset.id().equals(created.getContent()));
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

        @Test
        void should_get_an_asset_async() {

            var created = assets.createAsync(shouldCreateAnAssetRequest()).join();
            var result = assets.getAsync(created.getContent());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(asset -> {
                assertThat(asset.isSucceeded()).isTrue();
                assertThat(asset.getContent()).isNotNull();
                assertThat(asset.getContent().id()).isEqualTo(created.getContent());
            });
        }

        @Test
        void should_update_an_asset_async() {
            var created = assets.createAsync(shouldCreateAnAssetRequest()).join();

            var result = assets.updateAsync(shouldUpdateAnAssetRequest(created.getContent()));
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_delete_an_asset_async() {
            var created = assets.createAsync(shouldCreateAnAssetRequest()).join();

            var result = assets.deleteAsync(created.getContent());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_get_assets_async() {
            var created = assets.createAsync(shouldCreateAnAssetRequest()).join();

            var result = assets.requestAsync(shouldGetAssetsQuery());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(assetList -> {
                assertThat(assetList.getContent()).anyMatch(asset -> asset.id().equals(created.getContent()));
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

    private Asset shouldUpdateAnAssetRequest(String id) {
        var properties = Map.of("key1", Map.of("value", "value"));
        var dataAddress = Map.of("type", "data-address-type");

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
