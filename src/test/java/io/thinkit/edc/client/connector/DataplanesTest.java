package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class DataplanesTest {

    @Container
    private final ManagementApiContainer prism = new ManagementApiContainer();

    private final HttpClient http = HttpClient.newBuilder().build();
    private Dataplanes dataplanes;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        dataplanes = client.dataplanes();
    }

    @Test
    void should_get_dataplanes() {
        var dataplanesList = dataplanes.get();

        assertThat(dataplanesList.isSucceeded()).isTrue();
        assertThat(dataplanesList.getContent()).isNotNull().first().satisfies(dataPlaneInstance -> {
            assertThat(dataPlaneInstance.id()).isNotBlank();
            assertThat(dataPlaneInstance.allowedDestTypes().size()).isGreaterThan(0);
            assertThat(dataPlaneInstance.allowedDestTypes().get(0)).isEqualTo("your-dest-type");
            assertThat(dataPlaneInstance.allowedSourceTypes().size()).isGreaterThan(0);
            assertThat(dataPlaneInstance.allowedSourceTypes().get(0)).isEqualTo("source-type1");
            assertThat(dataPlaneInstance.allowedSourceTypes().get(1)).isEqualTo("source-type2");
            assertThat(dataPlaneInstance.url()).isEqualTo("http://somewhere.com:1234/api/v1");
        });
    }

    @Test
    void should_select_s_dataplane() {
        var source = DataAddress.Builder.newInstance().type("test-src1").build();
        var destination = DataAddress.Builder.newInstance().type("test-dst2").build();
        var selectionRequest = SelectionRequest.Builder.newInstance()
                .destination(destination)
                .source(source)
                .strategy("you_custom_strategy")
                .build();
        var dataplane = dataplanes.select(selectionRequest);

        assertThat(dataplane.isSucceeded()).isTrue();
        assertThat(dataplane.getContent()).isNotNull().satisfies(dataPlaneInstance -> {
            assertThat(dataPlaneInstance.id()).isNotBlank();
            assertThat(dataPlaneInstance.allowedDestTypes().size()).isGreaterThan(0);
            assertThat(dataPlaneInstance.allowedDestTypes().get(0)).isEqualTo("your-dest-type");
            assertThat(dataPlaneInstance.allowedSourceTypes().size()).isGreaterThan(0);
            assertThat(dataPlaneInstance.allowedSourceTypes().get(0)).isEqualTo("source-type1");
            assertThat(dataPlaneInstance.allowedSourceTypes().get(1)).isEqualTo("source-type2");
            assertThat(dataPlaneInstance.url()).isEqualTo("http://somewhere.com:1234/api/v1");
        });
    }
}
