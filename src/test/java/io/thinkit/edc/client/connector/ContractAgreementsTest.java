package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.utils.Constants.ODRL_NAMESPACE;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.ContractAgreement;
import io.thinkit.edc.client.connector.model.ContractNegotiation;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.services.ContractAgreements;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void should_get_a_contract_agreement() {
        var contractAgreement = contractAgreements.get("negotiation-id");

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

    @Test
    void should_get_a_contract_agreement_async() {
        try {
            Result<ContractAgreement> contractAgreement =
                    contractAgreements.getAsync("negotiation-id").get();

            assertThat(contractAgreement.isSucceeded()).isTrue();
            assertThat(contractAgreement.getContent().id()).isNotBlank();
            assertThat(contractAgreement.getContent().providerId()).isEqualTo("provider-id");
            assertThat(contractAgreement.getContent().consumerId()).isEqualTo("consumer-id");
            assertThat(contractAgreement.getContent().assetId()).isEqualTo("asset-id");
            assertThat(contractAgreement.getContent().contractSigningDate()).isGreaterThan(0);
            assertThat(contractAgreement.getContent().policy()).isNotNull().satisfies(policy -> assertThat(
                            policy.getList(ODRL_NAMESPACE + "permission").size())
                    .isGreaterThan(0));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_get_a_contract_agreement_when_id_is_empty() {
        var contractAgreement = contractAgreements.get("");

        assertThat(contractAgreement.isSucceeded()).isFalse();
        assertThat(contractAgreement.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_not_get_a_contract_agreement_when_id_is_empty_async() {
        try {
            Result<ContractAgreement> contractAgreement =
                    contractAgreements.getAsync("").get();
            assertThat(contractAgreement.isSucceeded()).isFalse();
            assertThat(contractAgreement.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_get_a_contract_agreement_negotiation() {
        var contractNegotiation = contractAgreements.getNegotiation("agreement-id");

        assertThat(contractNegotiation.isSucceeded()).isTrue();
        assertThat(contractNegotiation.getContent().id()).isNotBlank();
        assertThat(contractNegotiation.getContent().type()).isNotNull().satisfies(type -> assertThat(type)
                .isEqualTo("PROVIDER"));
        assertThat(contractNegotiation.getContent().protocol()).isNotNull().satisfies(protocol -> assertThat(protocol)
                .isEqualTo("dataspace-protocol-http"));
        assertThat(contractNegotiation.getContent().counterPartyId())
                .isNotNull()
                .satisfies(counterPartyId -> assertThat(counterPartyId).isEqualTo("counter-party-id"));
        assertThat(contractNegotiation.getContent().counterPartyAddress())
                .isNotNull()
                .satisfies(counterPartyAddress ->
                        assertThat(counterPartyAddress).isEqualTo("http://counter/party/address"));
        assertThat(contractNegotiation.getContent().state()).isNotNull().satisfies(state -> assertThat(state)
                .isEqualTo("VERIFIED"));
        assertThat(contractNegotiation.getContent().contractAgreementId())
                .isNotNull()
                .satisfies(
                        contractAgreementId -> assertThat(contractAgreementId).isEqualTo("contract:agreement:id"));
        assertThat(contractNegotiation.getContent().errorDetail())
                .isNotNull()
                .satisfies(errorDetail -> assertThat(errorDetail).isEqualTo("eventual-error-detail"));
        assertThat(contractNegotiation.getContent().callbackAddresses())
                .isNotNull()
                .first()
                .satisfies(callbackAddress -> {
                    assertThat(callbackAddress.authCodeId()).isNotNull().satisfies(authCodeId -> assertThat(authCodeId)
                            .isEqualTo("auth-code-id"));
                    assertThat(callbackAddress.authKey()).isNotNull().satisfies(authKey -> assertThat(authKey)
                            .isEqualTo("auth-key"));
                    assertThat(callbackAddress.transactional()).isNotNull().satisfies(transactional -> assertThat(
                                    transactional)
                            .isFalse());
                    assertThat(callbackAddress.uri()).isNotNull().satisfies(uri -> assertThat(uri)
                            .isEqualTo("http://callback/url"));
                    assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                        assertThat(uri.get(0)).isEqualTo("contract.negotiation");
                        assertThat(uri.get(1)).isEqualTo("transfer.process");
                    });
                });
        assertThat(contractNegotiation.getContent().createdAt()).isGreaterThan(0);
    }

    @Test
    void should_get_a_contract_agreement_negotiation_async() {
        try {
            Result<ContractNegotiation> contractNegotiation =
                    contractAgreements.getNegotiationAsync("agreement-id").get();
            assertThat(contractNegotiation.isSucceeded()).isTrue();
            assertThat(contractNegotiation.getContent().id()).isNotBlank();
            assertThat(contractNegotiation.getContent().type()).isNotNull().satisfies(type -> assertThat(type)
                    .isEqualTo("PROVIDER"));
            assertThat(contractNegotiation.getContent().protocol())
                    .isNotNull()
                    .satisfies(protocol -> assertThat(protocol).isEqualTo("dataspace-protocol-http"));
            assertThat(contractNegotiation.getContent().counterPartyId())
                    .isNotNull()
                    .satisfies(counterPartyId -> assertThat(counterPartyId).isEqualTo("counter-party-id"));
            assertThat(contractNegotiation.getContent().counterPartyAddress())
                    .isNotNull()
                    .satisfies(counterPartyAddress ->
                            assertThat(counterPartyAddress).isEqualTo("http://counter/party/address"));
            assertThat(contractNegotiation.getContent().state()).isNotNull().satisfies(state -> assertThat(state)
                    .isEqualTo("VERIFIED"));
            assertThat(contractNegotiation.getContent().contractAgreementId())
                    .isNotNull()
                    .satisfies(contractAgreementId ->
                            assertThat(contractAgreementId).isEqualTo("contract:agreement:id"));
            assertThat(contractNegotiation.getContent().errorDetail())
                    .isNotNull()
                    .satisfies(errorDetail -> assertThat(errorDetail).isEqualTo("eventual-error-detail"));
            assertThat(contractNegotiation.getContent().callbackAddresses())
                    .isNotNull()
                    .first()
                    .satisfies(callbackAddress -> {
                        assertThat(callbackAddress.authCodeId())
                                .isNotNull()
                                .satisfies(authCodeId -> assertThat(authCodeId).isEqualTo("auth-code-id"));
                        assertThat(callbackAddress.authKey()).isNotNull().satisfies(authKey -> assertThat(authKey)
                                .isEqualTo("auth-key"));
                        assertThat(callbackAddress.transactional()).isNotNull().satisfies(transactional -> assertThat(
                                        transactional)
                                .isFalse());
                        assertThat(callbackAddress.uri()).isNotNull().satisfies(uri -> assertThat(uri)
                                .isEqualTo("http://callback/url"));
                        assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                            assertThat(uri.get(0)).isEqualTo("contract.negotiation");
                            assertThat(uri.get(1)).isEqualTo("transfer.process");
                        });
                    });
            assertThat(contractNegotiation.getContent().createdAt()).isGreaterThan(0);

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_get_a_contract_agreement_negotiation_when_id_is_empty() {
        var contractNegotiation = contractAgreements.getNegotiation("");

        assertThat(contractNegotiation.isSucceeded()).isFalse();
        assertThat(contractNegotiation.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_not_get_a_contract_agreement_negotiation_when_id_is_empty_async() {
        try {
            Result<ContractNegotiation> contractNegotiation =
                    contractAgreements.getNegotiationAsync("").get();
            assertThat(contractNegotiation.isSucceeded()).isFalse();
            assertThat(contractNegotiation.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_get_a_contract_agreements() {
        var input = QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
        var contractAgreementList = contractAgreements.request(input);

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

    @Test
    void should_get_a_contract_agreements_async() {
        try {
            var input = QuerySpec.Builder.newInstance()
                    .offset(5)
                    .limit(10)
                    .sortOrder("DESC")
                    .sortField("fieldName")
                    .build();
            Result<List<ContractAgreement>> contractAgreementList =
                    contractAgreements.requestAsync(input).get();
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
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_not_get_contract_agreements() {
        var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

        var result = contractAgreements.request(input);

        assertThat(result.isSucceeded()).isFalse();
        assertThat(result.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_not_get_contract_agreements_async() {
        try {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            Result<List<ContractAgreement>> result =
                    contractAgreements.requestAsync(input).get();
            assertThat(result.isSucceeded()).isFalse();
            assertThat(result.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
                assertThat(apiErrorDetail.message()).isEqualTo("error message");
                assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
                assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
                assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
            });
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
