package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.utils.Constants.ODRL_NAMESPACE;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.ContractAgreement;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdQuerySpec;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ContractAgreementsTest extends ManagementApiTestBase {

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

    @Nested
    class Sync {
        @Test
        void should_get_a_contract_agreement() {
            var contractAgreement = contractAgreements.get("negotiation-id");
            assertThat(contractAgreement).satisfies(ContractAgreementsTest::shouldGetAContractAgreementResponse);
        }

        @Test
        void should_not_get_a_contract_agreement_when_id_is_empty() {
            var contractAgreement = contractAgreements.get("");
            assertThat(contractAgreement).satisfies(ContractAgreementsTest.this::errorResponse);
        }

        @Test
        void should_get_a_contract_agreement_negotiation() {
            var contractNegotiation = contractAgreements.getNegotiation("agreement-id");
            assertThat(contractNegotiation).satisfies(ContractNegotiationsTest::shouldGetAContractNegotiationResponse);
        }

        @Test
        void should_not_get_a_contract_agreement_negotiation_when_id_is_empty() {
            var contractNegotiation = contractAgreements.getNegotiation("");
            assertThat(contractNegotiation).satisfies(ContractAgreementsTest.this::errorResponse);
        }

        @Test
        void should_get_a_contract_agreements() {
            var contractAgreementList = contractAgreements.request(shouldGetAContractAgreementsQuery());
            assertThat(contractAgreementList)
                    .satisfies(ContractAgreementsTest.this::shouldGetAContractAgreementsResponse);
        }

        @Test
        void should_not_get_contract_agreements() {
            var input = JsonLdQuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = contractAgreements.request(input);
            assertThat(result).satisfies(ContractAgreementsTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_contract_agreement_async() {
            var contractAgreement = contractAgreements.getAsync("negotiation-id");
            assertThat(contractAgreement)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest::shouldGetAContractAgreementResponse);
        }

        @Test
        void should_not_get_a_contract_agreement_when_id_is_empty_async() {
            var result = contractAgreements.getAsync("");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest.this::errorResponse);
        }

        @Test
        void should_get_a_contract_agreement_negotiation_async() {
            var contractNegotiation = contractAgreements.getNegotiationAsync("agreement-id");

            assertThat(contractNegotiation)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest::shouldGetAContractNegotiationResponse);
        }

        @Test
        void should_not_get_a_contract_agreement_negotiation_when_id_is_empty_async() {
            var contractNegotiation = contractAgreements.getNegotiationAsync("");
            assertThat(contractNegotiation)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest.this::errorResponse);
        }

        @Test
        void should_get_a_contract_agreements_async() {

            var contractAgreementList = contractAgreements.requestAsync(shouldGetAContractAgreementsQuery());
            assertThat(contractAgreementList)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest.this::shouldGetAContractAgreementsResponse);
        }

        @Test
        void should_not_get_contract_agreements_async() {
            var input = JsonLdQuerySpec.Builder.newInstance().sortOrder("wrong").build();
            var result = contractAgreements.requestAsync(input);
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest.this::errorResponse);
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

    protected static void shouldGetAContractAgreementResponse(Result<ContractAgreement> contractAgreement) {

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

    private QuerySpec shouldGetAContractAgreementsQuery() {
        return JsonLdQuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
    }

    private void shouldGetAContractAgreementsResponse(Result<List<ContractAgreement>> contractAgreementList) {
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
}
