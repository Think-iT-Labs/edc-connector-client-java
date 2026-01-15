package io.thinkit.edc.client.connector.services.management;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.Edr;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EdrCacheTest extends ManagementApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private EdrCache edrCache;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        edrCache = client.edrCache();
    }

    @Nested
    class Sync {
        @Test
        void should_request_an_edr() {
            var result = edrCache.request(shouldRequestQuery());
            assertThat(result).satisfies(EdrCacheTest.this::shouldGetEdrResponse);
        }

        @Test
        void should__not_request_an_edr() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = edrCache.request(input);
            assertThat(result).satisfies(EdrCacheTest.this::errorResponse);
        }

        @Test
        void should_not_delete_an_edr_when_id_is_empty() {
            var deleted = edrCache.delete("");
            assertThat(deleted).satisfies(EdrCacheTest.this::errorResponse);
        }

        @Test
        void should_get_data_address() {
            var result = edrCache.dataAddress("transferProcessId");
            assertThat(result.isSucceeded()).isTrue();
        }

        @Test
        void should_not_get_data_address() {
            var result = edrCache.dataAddress("");
            assertThat(result).satisfies(EdrCacheTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_request_an_edr_async() {
            var result = edrCache.requestAsync(shouldRequestQuery());
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(EdrCacheTest.this::shouldGetEdrResponse);
        }

        @Test
        void should__not_request_an_edr_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = edrCache.requestAsync(input);
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(EdrCacheTest.this::errorResponse);
        }

        @Test
        void should_delete_an_edr_async() {

            var result = edrCache.deleteAsync("transferProcessId");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_not_delete_an_edr_when_id_is_empty_async() {
            var result = edrCache.deleteAsync("");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(EdrCacheTest.this::errorResponse);
        }

        @Test
        void should_get_data_address_async() {
            var result = edrCache.dataAddressAsync("transferProcessId");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).matches(Result::isSucceeded);
        }

        @Test
        void should_not_get_data_address_async() {
            var result = edrCache.dataAddressAsync("");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(EdrCacheTest.this::errorResponse);
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

    private void shouldGetEdrResponse(Result<Edr> edr) {
        assertThat(edr.isSucceeded()).isTrue();
        assertThat(edr.getContent().transferProcessId()).isNotBlank();
        assertThat(edr.getContent().agreementId()).isNotBlank();
        assertThat(edr.getContent().contractNegotiationId()).isNotBlank();
        assertThat(edr.getContent().assetId()).isNotBlank();
        assertThat(edr.getContent().providerId()).isNotBlank();
        assertThat(edr.getContent().createdAt()).isGreaterThan(0);
    }

    private QuerySpec shouldRequestQuery() {
        return QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .filterExpression(emptyList())
                .build();
    }
}
