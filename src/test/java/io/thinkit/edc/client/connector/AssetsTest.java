package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class AssetsTest {

    @Container
    private final ManagementApiContainer prism = new ManagementApiContainer();

    private final HttpClient http = HttpClient.newBuilder().build();
    private Assets assets;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl("http://127.0.0.1:%s".formatted(prism.getFirstMappedPort()))
                .build();
        assets = client.assets();
    }

    @Test
    void should_get_an_asset() {
        Result<Asset> asset = assets.get("123");

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
        assertThat(created.getError()).isNotNull();
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
        assertThat(created.getError()).isNotNull();
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
        assertThat(deleted.getError()).isNotNull();
    }

    @Test
    void should_get_assets() {
        var input = new QuerySpec(5, 10, "DESC", "fieldName", new CriterionInput[] {});
        Result<List<Asset>> assetsList = assets.request(input);
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
    void should_not_get_assets() {
        var input = new QuerySpec(0, 0, "", "", new CriterionInput[] {});
        Result<List<Asset>> assetsList = assets.request(input);

        assertThat(assetsList.isSucceeded()).isFalse();
        assertThat(assetsList.getError()).isNotNull();
    }
}
