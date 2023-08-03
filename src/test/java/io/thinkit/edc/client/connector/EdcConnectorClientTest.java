package io.thinkit.edc.client.connector;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class EdcConnectorClientTest {

    @Container
    private GenericContainer<?> prism = new GenericContainer<>("stoplight/prism:3.3.4")
            .withFileSystemBind(new File("").getAbsolutePath(),"/tmp")
            .withCommand("mock -h 0.0.0.0 -d https://api.swaggerhub.com/apis/eclipse-edc-bot/management-api/0.2.0")
            .withExposedPorts(4010);

    @Test
    void should_get_an_asset() {
        var client = new EdcConnectorClient("http://127.0.0.1:%s".formatted(prism.getFirstMappedPort()));

        var asset = client.getAsset("123");

        assertThat(asset).isNotNull();
    }

}
