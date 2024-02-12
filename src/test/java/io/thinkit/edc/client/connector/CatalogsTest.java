package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.utils.Constants.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.CatalogRequest;
import io.thinkit.edc.client.connector.model.DatasetRequest;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.services.Catalogs;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void should_get_catalog() {
        var query = QuerySpec.Builder.newInstance()
                .offset(0)
                .limit(50)
                .sortOrder("DESC")
                .sortField("fieldName")
                .filterExpression(emptyList())
                .build();
        CatalogRequest input = CatalogRequest.Builder.newInstance()
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .querySpec(query)
                .build();

        var result = catalogs.request(input);

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
                assertThat(distribution.format().getString(ID)).isEqualTo("HttpData");
            });
        });
    }

    @Test
    void should_get_catalog_async() {
        try {
            var query = QuerySpec.Builder.newInstance()
                    .offset(0)
                    .limit(50)
                    .sortOrder("DESC")
                    .sortField("fieldName")
                    .filterExpression(emptyList())
                    .build();
            CatalogRequest input = CatalogRequest.Builder.newInstance()
                    .protocol("dataspace-protocol-http")
                    .counterPartyAddress("http://provider-address")
                    .querySpec(query)
                    .build();

            var result = catalogs.requestAsync(input).get();

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
                    assertThat(distribution.format().getString(ID)).isEqualTo("HttpData");
                });
            });
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_get_dataset() {
        DatasetRequest input = DatasetRequest.Builder.newInstance()
                .id("dataset-id")
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .build();

        var result = catalogs.requestDataset(input);

        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent()).isNotNull().satisfies(dataset -> {
            assertThat(dataset.description()).isEqualTo("description");
            assertThat(dataset.hasPolicy()).isNotNull().satisfies(policy -> assertThat(
                            policy.getList(ODRL_NAMESPACE + "permission").size())
                    .isGreaterThan(0));
            assertThat(dataset.distribution()).isNotNull().first().satisfies(distribution -> {
                assertThat(distribution.accessService()).isNotBlank();
                assertThat(distribution.format().getString(ID)).isEqualTo("HttpData");
            });
        });
    }

    @Test
    void should_get_dataset_async() {
        try {
            DatasetRequest input = DatasetRequest.Builder.newInstance()
                    .id("dataset-id")
                    .protocol("dataspace-protocol-http")
                    .counterPartyAddress("http://provider-address")
                    .build();

            var result = catalogs.requestDatasetAsync(input).get();

            assertThat(result.isSucceeded()).isTrue();
            assertThat(result.getContent()).isNotNull().satisfies(dataset -> {
                assertThat(dataset.description()).isEqualTo("description");
                assertThat(dataset.hasPolicy()).isNotNull().satisfies(policy -> assertThat(
                                policy.getList(ODRL_NAMESPACE + "permission").size())
                        .isGreaterThan(0));
                assertThat(dataset.distribution()).isNotNull().first().satisfies(distribution -> {
                    assertThat(distribution.accessService()).isNotBlank();
                    assertThat(distribution.format().getString(ID)).isEqualTo("HttpData");
                });
            });
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
