package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
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
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedClass
@ValueSource(strings = {V3, V4BETA})
class DataplanesTest extends ManagementApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private Dataplanes dataplanes;

    DataplanesTest(String managementVersion) {
        this.managementVersion = managementVersion;
    }

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(prism.getUrl(), managementVersion)
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
            assertThat(dataPlaneInstance.allowedSourceTypes().size()).isGreaterThan(0);
            assertThat(dataPlaneInstance.allowedSourceTypes().get(0)).isNotBlank();
            assertThat(dataPlaneInstance.allowedTransferTypes().size()).isGreaterThan(0);
            assertThat(dataPlaneInstance.allowedTransferTypes().get(0)).isNotBlank();
            assertThat(dataPlaneInstance.state()).isNotBlank();
            assertThat(dataPlaneInstance.stateTimestamp()).isGreaterThan(0);
            assertThat(dataPlaneInstance.url()).isNotBlank();
        });
    }
}
