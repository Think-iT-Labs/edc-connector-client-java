package io.thinkit.edc.client.connector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.net.http.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class AssetsTest {

    @Container
    private GenericContainer<?> prism = new GenericContainer<>("stoplight/prism:3.3.4")
            .withFileSystemBind(new File("").getAbsolutePath(),"/tmp")
            .withCommand("mock -h 0.0.0.0 -d https://api.swaggerhub.com/apis/eclipse-edc-bot/management-api/0.2.0")
            .withExposedPorts(4010);
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
        var asset = assets.get("123");

        assertThat(asset.id()).isNotBlank();
        assertThat(asset.properties()).containsEntry("https://w3id.org/edc/v0.0.1/ns/key", "value");
        assertThat(asset.privateProperties()).containsEntry("https://w3id.org/edc/v0.0.1/ns/privateKey", "privateValue");
        assertThat(asset.dataAddress()).isNotNull().satisfies(dataAddress -> {
            assertThat(dataAddress.type()).isNotBlank();
            assertThat(dataAddress.properties()).isNotEmpty();
        });
        assertThat(asset.createdAt()).isGreaterThan(0);
    }

}
