package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.Asset;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.services.Assets;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void should_get_an_asset() {
        var asset = assets.get("123");

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

    @Test
    void should_create_an_asset() {
        var properties = Map.of("key", Map.of("value", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        var dataAddress = Map.of("type", "data-address-type");

        var asset = Asset.Builder.newInstance()
                .id("assetId")
                .properties(properties)
                .privateProperties(privateProperties)
                .dataAddress(dataAddress)
                .build();

        var created = assets.create(asset);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should_not_create_an_asset_when_data_address_is_missing() {
        var properties = Map.of("key", "value");
        var privateProperties = Map.of("private-key", "private-value");
        var asset = Asset.Builder.newInstance()
                .id("assetId")
                .properties(properties)
                .privateProperties(privateProperties)
                .build();

        var created = assets.create(asset);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_update_an_asset() {
        var properties = Map.of("key", Map.of("value", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        var dataAddress = Map.of("type", "data-address-type");
        var asset = Asset.Builder.newInstance()
                .id("assetId")
                .properties(properties)
                .privateProperties(privateProperties)
                .dataAddress(dataAddress)
                .build();

        var created = assets.update(asset);

        assertThat(created.isSucceeded()).isTrue();
    }

    @Test
    void should_not_update_an_asset_when_id_is_empty() {
        Map<String, Object> properties = Map.of("key", "value");
        Map<String, Object> privateProperties = Map.of("private-key", "private-value");
        var asset = Asset.Builder.newInstance()
                .id("")
                .properties(properties)
                .privateProperties(privateProperties)
                .build();

        var created = assets.update(asset);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_delete_an_asset() {
        var deleted = assets.delete("assetId");

        assertThat(deleted.isSucceeded()).isTrue();
    }

    @Test
    void should_not_delete_an_asset_when_id_is_empty() {
        var deleted = assets.delete("");

        assertThat(deleted.isSucceeded()).isFalse();
        assertThat(deleted.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_get_assets() {
        var query = QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .filterExpression(emptyList())
                .build();

        var assetsList = assets.request(query);

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

    @Test
    void should_not_get_assets_when_sort_schema_is_not_as_expected() {
        var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

        var assetsList = assets.request(input);

        assertThat(assetsList.isSucceeded()).isFalse();
        assertThat(assetsList.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_get_an_asset_async() {
        try {
            Result<Asset> asset = assets.getAsync("123").get();
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
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_create_an_asset_async() {
        var properties = Map.of("key", Map.of("value", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        var dataAddress = Map.of("type", "data-address-type");

        var asset = Asset.Builder.newInstance()
                .id("assetId")
                .properties(properties)
                .privateProperties(privateProperties)
                .dataAddress(dataAddress)
                .build();

        try {
            Result<String> created = assets.createAsync(asset).get();
            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_create_an_asset_when_data_address_is_missing_async() {
        var properties = Map.of("key", "value");
        var privateProperties = Map.of("private-key", "private-value");
        var asset = Asset.Builder.newInstance()
                .id("assetId")
                .properties(properties)
                .privateProperties(privateProperties)
                .build();
        try {
            Result<String> created = assets.createAsync(asset).get();
            assertThat(created.isSucceeded()).isFalse();
            assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_update_an_asset_async() {
        var properties = Map.of("key", Map.of("value", "value"));
        var privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        var dataAddress = Map.of("type", "data-address-type");
        var asset = Asset.Builder.newInstance()
                .id("assetId")
                .properties(properties)
                .privateProperties(privateProperties)
                .dataAddress(dataAddress)
                .build();
        try {
            var created = assets.updateAsync(asset).get();
            assertThat(created.isSucceeded()).isTrue();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_update_an_asset_when_id_is_empty_async() {
        Map<String, Object> properties = Map.of("key", "value");
        Map<String, Object> privateProperties = Map.of("private-key", "private-value");
        var asset = Asset.Builder.newInstance()
                .id("")
                .properties(properties)
                .privateProperties(privateProperties)
                .build();
        try {
            var created = assets.updateAsync(asset).get();

            assertThat(created.isSucceeded()).isFalse();
            assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_delete_an_asset_async() {

        try {
            var deleted = assets.deleteAsync("assetId").get();
            assertThat(deleted.isSucceeded()).isTrue();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_delete_an_asset_when_id_is_empty_async() {
        try {
            var deleted = assets.deleteAsync("").get();
            assertThat(deleted.isSucceeded()).isFalse();
            assertThat(deleted.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_get_assets_async() {
        var query = QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .filterExpression(emptyList())
                .build();
        try {

            var assetsList = assets.requestAsync(query).get();

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
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_get_assets_when_sort_schema_is_not_as_expected_async() {
        var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();
        try {
            var assetsList = assets.requestAsync(input).get();

            assertThat(assetsList.isSucceeded()).isFalse();
            assertThat(assetsList.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
