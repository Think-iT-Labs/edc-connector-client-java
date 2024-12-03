package io.thinkit.edc.client.connector.services;

import static io.thinkit.edc.client.connector.utils.Constants.ODRL_NAMESPACE;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.*;
import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CatalogsTest extends ManagementApiTestBase {

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

    @Nested
    class Sync {
        @Test
        void should_get_catalog() {
            var result = catalogs.request(shouldGetCatalogRequest());
            assertThat(result).satisfies(CatalogsTest.this::shouldGetCatalogResponse);
        }

        @Test
        void should_get_dataset() {

            var result = catalogs.requestDataset(shouldGetDatasetRequest());
            assertThat(result).satisfies(CatalogsTest.this::shouldGetDatasetResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_catalog_async() {
            var result = catalogs.requestAsync(shouldGetCatalogRequest());
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(CatalogsTest.this::shouldGetCatalogResponse);
        }

        @Test
        void should_get_dataset_async() {

            var result = catalogs.requestDatasetAsync(shouldGetDatasetRequest());
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(CatalogsTest.this::shouldGetDatasetResponse);
        }
    }

    private CatalogRequest shouldGetCatalogRequest() {
        var query = new QuerySpec(5, 10, "DESC", "fieldName", new CriterionInput[] {});
        return new CatalogRequest("dataspace-protocol-http", "http://provider-address", query);
    }

    private void shouldGetCatalogResponse(Result<Catalog> result) {
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

    private DatasetRequest shouldGetDatasetRequest() {
        return DatasetRequest.Builder.newInstance()
                .id("dataset-id")
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .build();
    }

    private void shouldGetDatasetResponse(Result<Dataset> result) {
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
}
