package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.utils.Constants.ID;
import static io.thinkit.edc.client.connector.utils.Constants.ODRL_NAMESPACE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.services.Catalogs;
import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CatalogsTest extends ContainerTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private Catalogs catalogs;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        catalogs = client.catalogs();
    }

    CatalogRequest should_get_catalog_request() {
        var query = QuerySpec.Builder.newInstance()
                .offset(0)
                .limit(50)
                .sortOrder("DESC")
                .sortField("fieldName")
                .filterExpression(emptyList())
                .build();
        return CatalogRequest.Builder.newInstance()
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .querySpec(query)
                .build();
    }

    void should_get_catalog_response(Result<Catalog> result) {
        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent().id()).isNotBlank();
        assertThat(result.getContent().participantId()).isNotNull().isEqualTo("urn:connector:provider");
        assertThat(result.getContent().service()).isNotNull().satisfies(service -> {
            assertThat(service.endpointUrl()).isEqualTo("http://localhost:16806/protocol");
            assertThat(service.terms()).isEqualTo("connector");
        });
        assertThat(result.getContent().dataset()).isNotNull().satisfies(dataset -> {
            assertThat(dataset.description()).isEqualTo("description");
            assertThat(dataset.hasPolicy()).isNotNull().satisfies(policy -> assertThat(
                            policy.getList(ODRL_NAMESPACE + "permission").size())
                    .isGreaterThan(0));
            assertThat(dataset.distribution()).isNotNull().first().satisfies(distribution -> {
                assertThat(distribution.accessService()).isNotBlank();
                assertThat(distribution.format()).isEqualTo("HttpData");
            });
        });
    }

    DatasetRequest should_get_dataset_request() {
        return DatasetRequest.Builder.newInstance()
                .id("dataset-id")
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .build();
    }

    void should_get_dataset_response(Result<Dataset> result) {
        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent()).isNotNull().satisfies(dataset -> {
            assertThat(dataset.description()).isEqualTo("description");
            assertThat(dataset.hasPolicy()).isNotNull().satisfies(policy -> assertThat(
                            policy.getList(ODRL_NAMESPACE + "permission").size())
                    .isGreaterThan(0));
            assertThat(dataset.distribution()).isNotNull().first().satisfies(distribution -> {
                assertThat(distribution.accessService()).isNotBlank();
                assertThat(distribution.format()).isEqualTo("HttpData");
            });
        });
    }

    @Nested
    class Sync {
        @Test
        void should_get_catalog() {
            var result = catalogs.request(should_get_catalog_request());
            should_get_catalog_response(result);
        }

        @Test
        void should_get_dataset() {

            var result = catalogs.requestDataset(should_get_dataset_request());
            should_get_dataset_response(result);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_catalog_async() {
            var result = catalogs.requestAsync(should_get_catalog_request());
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(CatalogsTest.this::should_get_catalog_response);
        }

        @Test
        void should_get_dataset_async() {

            var result = catalogs.requestDatasetAsync(should_get_dataset_request());
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(CatalogsTest.this::should_get_dataset_response);
        }
    }
}
