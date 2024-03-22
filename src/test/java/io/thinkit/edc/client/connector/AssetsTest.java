package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.services.Assets;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AssetsTest extends ContainerTestBase {

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

    <T> void error_response(Result<T> error) {
        assertThat(error.isSucceeded()).isFalse();
        assertThat(error.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    void should_get_an_asset_response(Result<Asset> asset) {
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

    void should_get_assets_response(Result<List<Asset>> assetsList) {
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

    Asset should_create_an_asset_request() {
        var properties = Map.of("key", Map.of("value", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        var dataAddress = Map.of("type", "data-address-type");

        return Asset.Builder.newInstance()
                .id("assetId")
                .properties(properties)
                .privateProperties(privateProperties)
                .dataAddress(dataAddress)
                .build();
    }

    Asset should_not_create_an_asset_request() {
        var properties = Map.of("key", Map.of("value", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        var dataAddress = Map.of("type", "data-address-type");

        return Asset.Builder.newInstance()
                .id("")
                .properties(properties)
                .privateProperties(privateProperties)
                .build();
    }

    QuerySpec should_get_assets_query() {
        return QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .filterExpression(emptyList())
                .build();
    }

    @Nested
    class Sync {
        @Test
        void should_get_an_asset() {
            var asset = assets.get("123");

            should_get_an_asset_response(asset);
        }

        @Test
        void should_create_an_asset() {

            var created = assets.create(should_create_an_asset_request());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_not_create_an_asset_when_data_address_is_missing() {

            var created = assets.create(should_not_create_an_asset_request());
            error_response(created);
        }

        @Test
        void should_update_an_asset() {
            var created = assets.update(should_create_an_asset_request());

            assertThat(created.isSucceeded()).isTrue();
        }

        @Test
        void should_not_update_an_asset_when_id_is_empty() {

            var created = assets.update(should_not_create_an_asset_request());
            error_response(created);
        }

        @Test
        void should_delete_an_asset() {
            var deleted = assets.delete("assetId");

            assertThat(deleted.isSucceeded()).isTrue();
        }

        @Test
        void should_not_delete_an_asset_when_id_is_empty() {
            var deleted = assets.delete("");
            error_response(deleted);
        }

        @Test
        void should_get_assets() {

            var assetsList = assets.request(should_get_assets_query());
            should_get_assets_response(assetsList);
        }

        @Test
        void should_not_get_assets_when_sort_schema_is_not_as_expected() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var assetsList = assets.request(input);
            error_response(assetsList);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_an_asset_async() {
            var result = assets.getAsync("123");
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(AssetsTest.this::should_get_an_asset_response);
        }

        @Test
        void should_create_an_asset_async() {

            var result = assets.createAsync(should_create_an_asset_request());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_create_an_asset_when_data_address_is_missing_async() {
            var result = assets.createAsync(should_not_create_an_asset_request());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(AssetsTest.this::error_response);
        }

        @Test
        void should_update_an_asset_async() {

            var result = assets.updateAsync(should_create_an_asset_request());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
            });
        }

        @Test
        void should_not_update_an_asset_when_id_is_empty_async() {

            var result = assets.updateAsync(should_not_create_an_asset_request());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(AssetsTest.this::error_response);
        }

        @Test
        void should_delete_an_asset_async() {

            var result = assets.deleteAsync("assetId");
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(deleted -> {
                assertThat(deleted.isSucceeded()).isTrue();
            });
        }

        @Test
        void should_not_delete_an_asset_when_id_is_empty_async() {
            var result = assets.deleteAsync("");
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(AssetsTest.this::error_response);
        }

        @Test
        void should_get_assets_async() {
            var result = assets.requestAsync(should_get_assets_query());
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(AssetsTest.this::should_get_assets_response);
        }

        @Test
        void should_not_get_assets_when_sort_schema_is_not_as_expected_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();
            var result = assets.requestAsync(input);
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(AssetsTest.this::error_response);
        }
    }
}
