package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AssetsTest extends ManagementApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private Assets assets;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        assets = client.assets();
    }

    @Nested
    class Sync {
        @Test
        void should_get_an_asset() {
            var asset = assets.get("123");
            assertThat(asset).satisfies(AssetsTest.this::shouldGetAnAssetResponse);
        }

        @Test
        void should_create_an_asset() {

            var created = assets.create(shouldCreateAnAssetRequest());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_not_create_an_asset_when_data_address_is_missing() {

            var created = assets.create(shouldNotCreateAnAssetRequest());
            assertThat(created).satisfies(AssetsTest.this::errorResponse);
        }

        @Test
        void should_update_an_asset() {
            var created = assets.update(shouldCreateAnAssetRequest());

            assertThat(created.isSucceeded()).isTrue();
        }

        @Test
        void should_not_update_an_asset_when_id_is_empty() {

            var created = assets.update(shouldNotCreateAnAssetRequest());
            assertThat(created).satisfies(AssetsTest.this::errorResponse);
        }

        @Test
        void should_delete_an_asset() {
            var deleted = assets.delete("assetId");

            assertThat(deleted.isSucceeded()).isTrue();
        }

        @Test
        void should_not_delete_an_asset_when_id_is_empty() {
            var deleted = assets.delete("");
            assertThat(deleted).satisfies(AssetsTest.this::errorResponse);
        }

        @Test
        void should_get_assets() {

            var assetsList = assets.request(shouldGetAssetsQuery());
            assertThat(assetsList).satisfies(AssetsTest.this::shouldGetAssetsResponse);
        }

        @Test
        void should_not_get_assets_when_sort_schema_is_not_as_expected() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var assetsList = assets.request(input);
            assertThat(assetsList).satisfies(AssetsTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_an_asset_async() {
            var result = assets.getAsync("123");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(AssetsTest.this::shouldGetAnAssetResponse);
        }

        @Test
        void should_create_an_asset_async() {

            var result = assets.createAsync(shouldCreateAnAssetRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_create_an_asset_when_data_address_is_missing_async() {
            var result = assets.createAsync(shouldNotCreateAnAssetRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(AssetsTest.this::errorResponse);
        }

        @Test
        void should_update_an_asset_async() {

            var result = assets.updateAsync(shouldCreateAnAssetRequest());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_not_update_an_asset_when_id_is_empty_async() {

            var result = assets.updateAsync(shouldNotCreateAnAssetRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(AssetsTest.this::errorResponse);
        }

        @Test
        void should_delete_an_asset_async() {

            var result = assets.deleteAsync("assetId");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_not_delete_an_asset_when_id_is_empty_async() {
            var result = assets.deleteAsync("");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(AssetsTest.this::errorResponse);
        }

        @Test
        void should_get_assets_async() {
            var result = assets.requestAsync(shouldGetAssetsQuery());
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(AssetsTest.this::shouldGetAssetsResponse);
        }

        @Test
        void should_not_get_assets_when_sort_schema_is_not_as_expected_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();
            var result = assets.requestAsync(input);
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(AssetsTest.this::errorResponse);
        }
    }

    private <T> void errorResponse(Result<T> error) {
        assertThat(error.isSucceeded()).isFalse();
        assertThat(error.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    private void shouldGetAnAssetResponse(Result<Asset> asset) {
        assertThat(asset.getContent().id()).isNotBlank();
        assertThat(asset.getContent().properties()).isNotNull().satisfies(properties -> {
            assertThat(properties.size()).isGreaterThan(0);
            assertThat(properties.getString(EDC_NAMESPACE + "key")).isEqualTo("value");
            assertThat(properties.getString("key")).isEqualTo("value");
            assertThat(properties.getString("not-existent")).isEqualTo(null);
        });
        assertThat(asset.getContent().privateProperties()).isNotNull().satisfies(privateProperties -> {
            assertThat(privateProperties.size()).isGreaterThan(0);
            assertThat(privateProperties.getString(EDC_NAMESPACE + "privateKey"))
                    .isEqualTo("privateValue");
            assertThat(privateProperties.getString("privateKey")).isEqualTo("privateValue");
            assertThat(privateProperties.getString("not-existent")).isEqualTo(null);
        });
        assertThat(asset.getContent().dataAddress()).isNotNull().satisfies(dataAddress -> {
            assertThat(dataAddress.type()).isNotBlank();
            assertThat(dataAddress.properties().size()).isGreaterThan(0);
        });
        assertThat(asset.getContent().createdAt()).isGreaterThan(0);
    }

    private void shouldGetAssetsResponse(Result<List<Asset>> assetsList) {
        assertThat(assetsList.isSucceeded()).isTrue();
        assertThat(assetsList.getContent()).isNotNull().first().satisfies(asset -> {
            assertThat(asset.id()).isNotBlank();
            assertThat(asset.properties()).isNotNull().satisfies(properties -> {
                assertThat(properties.size()).isGreaterThan(0);
                assertThat(properties.getString(EDC_NAMESPACE + "key")).isEqualTo("value");
                assertThat(properties.getString("key")).isEqualTo("value");
                assertThat(properties.getString("not-existent")).isEqualTo(null);
            });
            assertThat(asset.privateProperties()).isNotNull().satisfies(privateProperties -> {
                assertThat(privateProperties.size()).isGreaterThan(0);
                assertThat(privateProperties.getString(EDC_NAMESPACE + "privateKey"))
                        .isEqualTo("privateValue");
                assertThat(privateProperties.getString("privateKey")).isEqualTo("privateValue");
                assertThat(privateProperties.getString("not-existent")).isEqualTo(null);
            });
            assertThat(asset.dataAddress()).isNotNull().satisfies(dataAddress -> {
                assertThat(dataAddress.type()).isNotBlank();
                assertThat(dataAddress.properties().size()).isGreaterThan(0);
            });
            assertThat(asset.createdAt()).isGreaterThan(0);
        });
    }

    private Asset shouldCreateAnAssetRequest() {
        var properties = Map.of(
                "key", Map.of("value", "value"),
                "key-with-id", Map.of("@id", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        var dataAddress = Map.of("type", "data-address-type");

        return Asset.Builder.newInstance()
                .id("assetId")
                .properties(properties)
                .privateProperties(privateProperties)
                .dataAddress(dataAddress)
                .build();
    }

    private Asset shouldNotCreateAnAssetRequest() {
        var properties = Map.of("key", Map.of("value", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));

        return Asset.Builder.newInstance()
                .id("")
                .properties(properties)
                .privateProperties(privateProperties)
                .build();
    }

    private QuerySpec shouldGetAssetsQuery() {
        return QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .filterExpression(emptyList())
                .build();
    }
}
