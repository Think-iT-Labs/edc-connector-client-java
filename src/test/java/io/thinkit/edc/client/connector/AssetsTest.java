package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.http.HttpClient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
class AssetsTest {

    @Container
    private GenericContainer<?> prism = new GenericContainer<>("stoplight/prism:3.3.4")
            .withFileSystemBind(new File("").getAbsolutePath(), "/tmp")
            .withCopyToContainer(MountableFile.forClasspathResource("/edcOpenAPi.yaml"), "/edcOpenAPi.yaml")
            .withCommand("mock -h 0.0.0.0 -d /edcOpenAPi.yaml")
            .withExposedPorts(4010)
            .withLogConsumer(frame -> {
                if (!frame.getUtf8String().contains("[CLI]")) {
                    System.out.println(frame.getUtf8String());
                }
            });

    private final HttpClient http = HttpClient.newBuilder().build();
    private Assets assets;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .interceptor(r -> r.header("Prefer", "dynamic=false"))
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
        Map<String, Object> properties = Map.of("key", Map.of("value", "value"));
        Map<String, Object> privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        Map<String, Object> dataAddress = Map.of("type", "data-address-type");
        var assetInput = new AssetInput("assetId", properties, privateProperties, dataAddress);

        Result<String> created = assets.create(assetInput);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should_not_create_an_asset_when_dataAddress_is_empty() {
        Map<String, Object> properties = Map.of("key", "value");
        Map<String, Object> privateProperties = Map.of("private-key", "private-value");
        Map<String, Object> dataAddress = Collections.emptyMap();
        var assetInput = new AssetInput("assetId", properties, privateProperties, dataAddress);

        Result<String> created = assets.create(assetInput);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getError()).isNotNull();
    }

    @Test
    void should_update_an_asset() {
        Map<String, Object> properties = Map.of("key", Map.of("value", "value"));
        Map<String, Object> privateProperties = Map.of("private-key", Map.of("private-value", "private-value"));
        Map<String, Object> dataAddress = Map.of("type", "data-address-type");
        var assetInput = new AssetInput("assetId", properties, privateProperties, dataAddress);

        Result<String> created = assets.update(assetInput);

        assertThat(created.isSucceeded()).isTrue();
    }

    @Test
    void should_not_update_an_asset_when_id_is_empty() {
        Map<String, Object> properties = Map.of("key", "value");
        Map<String, Object> privateProperties = Map.of("private-key", "private-value");
        Map<String, Object> dataAddress = Collections.emptyMap();
        var assetInput = new AssetInput("", properties, privateProperties, dataAddress);

        Result<String> created = assets.update(assetInput);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getError()).isNotNull();
    }

    @Test
    void should_delete_an_asset() {

        Result<String> deleted = assets.delete("assetId");

        assertThat(deleted.isSucceeded()).isTrue();
    }

    @Test
    void should_not_delete_an_asset_when_id_is_empty() {
        Result<String> deleted = assets.delete("");

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
