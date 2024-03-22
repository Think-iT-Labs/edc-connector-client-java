package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.ContractNegotiationsTest.should_get_a_contract_negotiation_response;
import static io.thinkit.edc.client.connector.utils.Constants.ODRL_NAMESPACE;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.ContractAgreement;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.services.ContractAgreements;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ContractAgreementsTest extends ContainerTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private ContractAgreements contractAgreements;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        contractAgreements = client.contractAgreements();
    }

    <T> void error_response(Result<T> error) {
        assertThat(error.isSucceeded()).isFalse();
        assertThat(error.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    protected static void should_get_a_contract_agreement_response(Result<ContractAgreement> contractAgreement) {

        assertThat(contractAgreement.isSucceeded()).isTrue();
        assertThat(contractAgreement.getContent().id()).isNotBlank();
        assertThat(contractAgreement.getContent().providerId()).isEqualTo("provider-id");
        assertThat(contractAgreement.getContent().consumerId()).isEqualTo("consumer-id");
        assertThat(contractAgreement.getContent().assetId()).isEqualTo("asset-id");
        assertThat(contractAgreement.getContent().contractSigningDate()).isGreaterThan(0);
        assertThat(contractAgreement.getContent().policy()).isNotNull().satisfies(policy -> assertThat(
                        policy.getList(ODRL_NAMESPACE + "permission").size())
                .isGreaterThan(0));
    }

    QuerySpec should_get_a_contract_agreements_query() {
        return QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
    }

    void should_get_a_contract_agreements_response(Result<List<ContractAgreement>> contractAgreementList) {
        assertThat(contractAgreementList.isSucceeded()).isTrue();
        assertThat(contractAgreementList.getContent()).isNotNull().first().satisfies(contractAgreement -> {
            assertThat(contractAgreement.id()).isNotBlank();
            assertThat(contractAgreement.providerId()).isEqualTo("provider-id");
            assertThat(contractAgreement.consumerId()).isEqualTo("consumer-id");
            assertThat(contractAgreement.assetId()).isEqualTo("asset-id");
            assertThat(contractAgreement.contractSigningDate()).isGreaterThan(0);
            assertThat(contractAgreement.policy()).isNotNull().satisfies(policy -> assertThat(
                            policy.getList(ODRL_NAMESPACE + "permission").size())
                    .isGreaterThan(0));
        });
    }

    @Nested
    class Sync {
        @Test
        void should_get_a_contract_agreement() {
            var contractAgreement = contractAgreements.get("negotiation-id");
            should_get_a_contract_agreement_response(contractAgreement);
        }

        @Test
        void should_not_get_a_contract_agreement_when_id_is_empty() {
            var contractAgreement = contractAgreements.get("");
            error_response(contractAgreement);
        }

        @Test
        void should_get_a_contract_agreement_negotiation() {
            var contractNegotiation = contractAgreements.getNegotiation("agreement-id");
            should_get_a_contract_negotiation_response(contractNegotiation);
        }

        @Test
        void should_not_get_a_contract_agreement_negotiation_when_id_is_empty() {
            var contractNegotiation = contractAgreements.getNegotiation("");
            error_response(contractNegotiation);
        }

        @Test
        void should_get_a_contract_agreements() {
            var contractAgreementList = contractAgreements.request(should_get_a_contract_agreements_query());
            should_get_a_contract_agreements_response(contractAgreementList);
        }

        @Test
        void should_not_get_contract_agreements() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = contractAgreements.request(input);
            error_response(result);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_contract_agreement_async() {
            var contractAgreement = contractAgreements.getAsync("negotiation-id");
            assertThat(contractAgreement)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest::should_get_a_contract_agreement_response);
        }

        @Test
        void should_not_get_a_contract_agreement_when_id_is_empty_async() {
            var result = contractAgreements.getAsync("");
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest.this::error_response);
        }

        @Test
        void should_get_a_contract_agreement_negotiation_async() {
            var contractNegotiation = contractAgreements.getNegotiationAsync("agreement-id");

            assertThat(contractNegotiation)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest::should_get_a_contract_negotiation_response);
        }

        @Test
        void should_not_get_a_contract_agreement_negotiation_when_id_is_empty_async() {
            var contractNegotiation = contractAgreements.getNegotiationAsync("");
            assertThat(contractNegotiation)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest.this::error_response);
        }

        @Test
        void should_get_a_contract_agreements_async() {

            var contractAgreementList = contractAgreements.requestAsync(should_get_a_contract_agreements_query());
            assertThat(contractAgreementList)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest.this::should_get_a_contract_agreements_response);
        }

        @Test
        void should_not_get_contract_agreements_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();
            var result = contractAgreements.requestAsync(input);
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest.this::error_response);
        }
    }
}
