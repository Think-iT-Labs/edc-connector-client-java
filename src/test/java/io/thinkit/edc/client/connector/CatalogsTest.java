package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class CatalogsTest {

    @Container
    private final ManagementApiContainer prism = new ManagementApiContainer();

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
}
