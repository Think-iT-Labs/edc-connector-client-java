package io.thinkit.edc.client.connector.services;

import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.CatalogApiTestBase;
import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.model.Catalog;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
import java.net.http.HttpClient;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CatalogCacheTest extends CatalogApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private CatalogCache catalogCache;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .catalogCacheUrl(prism.getUrl())
                .build();
        catalogCache = client.catalogCache();
    }

    @Nested
    class Sync {

        @Test
        void should_query_catalogs() {
            var query = JsonLdQuerySpec.Builder.newInstance()
                    .offset(0)
                    .limit(50)
                    .sortOrder("DESC")
                    .sortField("fieldName")
                    .filterExpression(emptyList())
                    .build();

            var result = catalogCache.query(query);

            assertThat(result).satisfies(CatalogCacheTest.this::assertCatalogs);
        }
    }

    @Nested
    class Async {
        @Test
        void should_query_catalogs() {
            var query = JsonLdQuerySpec.Builder.newInstance()
                    .offset(0)
                    .limit(50)
                    .sortOrder("DESC")
                    .sortField("fieldName")
                    .filterExpression(emptyList())
                    .build();

            var result = catalogCache.queryAsync(query);

            assertThat(result).succeedsWithin(timeout, SECONDS).satisfies(CatalogCacheTest.this::assertCatalogs);
        }
    }

    private void assertCatalogs(Result<List<Catalog>> result) {
        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent()).isNotNull().isNotEmpty().first().satisfies(catalog -> {
            assertThat(catalog.participantId()).isNotNull().isEqualTo("urn:connector:provider");
            assertThat(catalog.service()).isNotNull().satisfies(service -> {
                assertThat(service.endpointUrl()).isEqualTo("http://localhost:16806/protocol");
                assertThat(service.terms()).isEqualTo("connector");
            });
            assertThat(catalog.dataset()).isNotNull().satisfies(dataset -> {
                assertThat(dataset.description()).isEqualTo("description");
                assertThat(dataset.hasPolicy()).isNotNull();
                assertThat(dataset.distribution()).isNotNull().first().satisfies(distribution -> {
                    assertThat(distribution.accessService()).isNotBlank();
                    assertThat(distribution.format()).isEqualTo("HttpData");
                });
            });
        });
    }
}
