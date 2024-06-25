package io.thinkit.edc.client.connector.services;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.ContainerTestBase;
import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.model.DataAddress;
import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.SelectionRequest;

import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DataplanesTest extends ContainerTestBase {

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

        @Test
        void should_create_a_dataplane() {
            var created = dataplanes.create(shouldCreateADataplaneRequest());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_not_create_a_dataplane() {

            var created = dataplanes.create(shouldNotCreateADataplaneRequest());
            assertThat(created).satisfies(DataplanesTest.this::errorResponse);
        }

        @Test
        void should_select_a_dataplane() {

            var dataplane = dataplanes.select(shouldSelectADataplaneRequest());
            assertThat(dataplane).satisfies(DataplanesTest.this::shouldSelectADataplaneResponse);
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

        @Test
        void should_create_a_dataplane_async() {

            var result = dataplanes.createAsync(shouldCreateADataplaneRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_create_a_dataplane_async() {
            var created = dataplanes.createAsync(shouldNotCreateADataplaneRequest());
            assertThat(created).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(DataplanesTest.this::errorResponse);
        }

        @Test
        void should_select_a_dataplane_async() {

            var dataplane = dataplanes.selectAsync(shouldSelectADataplaneRequest());
            assertThat(dataplane)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(DataplanesTest.this::shouldSelectADataplaneResponse);
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

    private DataPlaneInstance shouldCreateADataplaneRequest() {
        return DataPlaneInstance.Builder.newInstance()
                .id("your-dataplane-id")
                .allowedDestTypes(Arrays.asList("your-dest-type", "your-dest-type2"))
                .allowedSourceTypes(Arrays.asList("source-type1", "source-type2"))
                .url("http://somewhere.com:1234/api/v1")
                .build();
    }

    private DataPlaneInstance shouldNotCreateADataplaneRequest() {
        return DataPlaneInstance.Builder.newInstance()
                .id("your-dataplane-id")
                .allowedSourceTypes(Arrays.asList("source-type1", "source-type2"))
                .url("http://somewhere.com:1234/api/v1")
                .build();
    }

    private SelectionRequest shouldSelectADataplaneRequest() {
        var source = DataAddress.Builder.newInstance().type("test-src1").build();
        var destination = DataAddress.Builder.newInstance().type("test-dst2").build();
        return SelectionRequest.Builder.newInstance()
                .destination(destination)
                .source(source)
                .strategy("you_custom_strategy")
                .build();
    }

    private void shouldSelectADataplaneResponse(Result<DataPlaneInstance> dataplane) {
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
