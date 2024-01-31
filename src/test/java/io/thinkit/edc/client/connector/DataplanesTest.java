package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.DataAddress;
import io.thinkit.edc.client.connector.model.DataPlaneInstance;
import io.thinkit.edc.client.connector.model.SelectionRequest;
import io.thinkit.edc.client.connector.services.Dataplanes;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
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
    void should_get_dataplanes_async() {
        try {
            var dataplanesList = dataplanes.getAsync().get();

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
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_create_a_dataplane() {

        var dataplane = DataPlaneInstance.Builder.newInstance()
                .id("your-dataplane-id")
                .allowedDestTypes(Arrays.asList("your-dest-type", "your-dest-type2"))
                .allowedSourceTypes(Arrays.asList("source-type1", "source-type2"))
                .url("http://somewhere.com:1234/api/v1")
                .build();

        var created = dataplanes.create(dataplane);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should_create_a_dataplane_async() {
        try {

            var dataplane = DataPlaneInstance.Builder.newInstance()
                    .id("your-dataplane-id")
                    .allowedDestTypes(Arrays.asList("your-dest-type", "your-dest-type2"))
                    .allowedSourceTypes(Arrays.asList("source-type1", "source-type2"))
                    .url("http://somewhere.com:1234/api/v1")
                    .build();

            var created = dataplanes.createAsync(dataplane).get();

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_create_a_dataplane() {

        var dataplane = DataPlaneInstance.Builder.newInstance()
                .id("your-dataplane-id")
                .allowedSourceTypes(Arrays.asList("source-type1", "source-type2"))
                .url("http://somewhere.com:1234/api/v1")
                .build();

        var created = dataplanes.create(dataplane);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_not_create_a_dataplane_async() {
        try {
            var dataplane = DataPlaneInstance.Builder.newInstance()
                    .id("your-dataplane-id")
                    .allowedSourceTypes(Arrays.asList("source-type1", "source-type2"))
                    .url("http://somewhere.com:1234/api/v1")
                    .build();

            var created = dataplanes.createAsync(dataplane).get();

            assertThat(created.isSucceeded()).isFalse();
            assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_select_a_dataplane() {
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

    @Test
    void should_select_a_dataplane_async() {
        try {
            var source = DataAddress.Builder.newInstance().type("test-src1").build();
            var destination =
                    DataAddress.Builder.newInstance().type("test-dst2").build();
            var selectionRequest = SelectionRequest.Builder.newInstance()
                    .destination(destination)
                    .source(source)
                    .strategy("you_custom_strategy")
                    .build();
            var dataplane = dataplanes.selectAsync(selectionRequest).get();

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
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
