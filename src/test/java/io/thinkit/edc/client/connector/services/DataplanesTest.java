package io.thinkit.edc.client.connector.services;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import io.thinkit.edc.client.connector.model.Result;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DataplanesTest extends ManagementApiTestBase {

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

    @Nested
    class Sync {
        @Test
        void should_get_dataplanes() {
            var dataplanesList = dataplanes.get();
            assertThat(dataplanesList).satisfies(DataplanesTest.this::shouldGetDataplanesResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_dataplanes_async() {
            var dataplanesList = dataplanes.getAsync();
            assertThat(dataplanesList)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(DataplanesTest.this::shouldGetDataplanesResponse);
        }
    }

    private void shouldGetDataplanesResponse(Result<List<DataPlaneInstance>> dataplanesList) {
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
}
