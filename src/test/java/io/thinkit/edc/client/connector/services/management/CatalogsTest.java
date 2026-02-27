package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdCatalogRequest;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
import io.thinkit.edc.client.connector.model.pojo.PojoCatalogRequest;
import io.thinkit.edc.client.connector.model.pojo.PojoQuerySpec;
import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedClass
@ValueSource(strings = {V3, V4BETA})
class CatalogsTest extends ManagementApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private Catalogs catalogs;

    CatalogsTest(String managementVersion) {
        this.managementVersion = managementVersion;
    }

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(prism.getUrl(), managementVersion)
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

            var result = catalogs.requestDataset(shouldGetCatalogRequest());
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

            var result = catalogs.requestDatasetAsync(shouldGetCatalogRequest());
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(CatalogsTest.this::shouldGetDatasetResponse);
        }
    }

    private CatalogRequest shouldGetCatalogRequest() {
        if (V3.equals(managementVersion)) {
            var query = JsonLdQuerySpec.Builder.newInstance()
                    .offset(0)
                    .limit(50)
                    .sortOrder("DESC")
                    .sortField("fieldName")
                    .filterExpression(emptyList())
                    .build();
            return JsonLdCatalogRequest.Builder.newInstance()
                    .protocol("dataspace-protocol-http")
                    .counterPartyId("counterPartyId")
                    .counterPartyAddress("http://provider-address")
                    .querySpec(query)
                    .build();
        } else {
            var query = PojoQuerySpec.Builder.newInstance()
                    .offset(0)
                    .limit(50)
                    .sortOrder("DESC")
                    .sortField("fieldName")
                    .filterExpression(emptyList())
                    .build();
            return PojoCatalogRequest.Builder.newInstance()
                    .id("string")
                    .protocol("dataspace-protocol-http")
                    .counterPartyId("counterPartyId")
                    .counterPartyAddress("http://provider-address")
                    .querySpec(query)
                    .build();
        }
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
            assertThat(dataset.hasPolicy()).isNotNull();
            assertThat(dataset.distribution()).isNotNull().first().satisfies(distribution -> {
                assertThat(distribution.accessService()).isNotBlank();
                assertThat(distribution.format()).isEqualTo("HttpData");
            });
        });
    }


    private void shouldGetDatasetResponse(Result<Dataset> result) {
        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent()).isNotNull().satisfies(dataset -> {
            assertThat(dataset.description()).isEqualTo("description");
            assertThat(dataset.hasPolicy()).isNotNull();
            assertThat(dataset.distribution()).isNotNull().first().satisfies(distribution -> {
                assertThat(distribution.accessService()).isNotBlank();
                assertThat(distribution.format()).isEqualTo("HttpData");
            });
        });
    }
}
