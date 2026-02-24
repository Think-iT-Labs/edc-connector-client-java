package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.ContractNegotiation;
import io.thinkit.edc.client.connector.model.ContractRequest;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.TerminateNegotiation;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdContractRequest;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdPolicy;
import io.thinkit.edc.client.connector.model.pojo.PojoContractRequest;
import io.thinkit.edc.client.connector.model.pojo.PojoPolicy;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedClass
@ValueSource(strings = {V3, V4BETA})
class ContractNegotiationsTest extends ManagementApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private ContractNegotiations contractNegotiations;

    ContractNegotiationsTest(String managementVersion) {
        this.managementVersion = managementVersion;
    }

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(prism.getUrl(), managementVersion)
                .build();
        contractNegotiations = client.contractNegotiations();
    }

    @Nested
    class Sync {
        @Test
        void should_get_a_contract_negotiation() {
            var contractNegotiation = contractNegotiations.get("negotiation-id");
            assertThat(contractNegotiation).satisfies(ContractNegotiationsTest::shouldGetAContractNegotiationResponse);
        }

        @Test
        void should_not_get_a_contract_negotiation_when_id_is_empty() {
            var contractNegotiation = contractNegotiations.get("");
            assertThat(contractNegotiation).satisfies(ContractNegotiationsTest.this::errorResponse);
        }

        @Test
        void should_create_a_contract_negotiation() {
            var created = contractNegotiations.create(shouldCreateAContractNegotiationRequest());
            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        @Disabled
        void should_get_a_contract_negotiation_attached_agreement() {
            var contractAgreement = contractNegotiations.getAgreement("negotiation-id");
            assertThat(contractAgreement).satisfies(ContractAgreementsTest::shouldGetAContractAgreementResponse);
        }

        @Test
        void should_not_get_a_contract_negotiation_attached_agreement() {
            var contractAgreement = contractNegotiations.getAgreement("");
            assertThat(contractAgreement).satisfies(ContractNegotiationsTest.this::errorResponse);
        }

        @Test
        void should_terminate_a_contract_negotiation() {
            var terminated = contractNegotiations.terminate(shouldTerminateAContractNegotiationRequest());
            assertThat(terminated.isSucceeded()).isTrue();
            assertThat(terminated.getContent()).isNotNull();
        }

        @Test
        void should_not_terminate_a_contract_negotiation_when_id_is_empty() {

            var terminated = contractNegotiations.terminate(shouldNotTerminateAContractNegotiationRequest());
            assertThat(terminated).satisfies(ContractNegotiationsTest.this::errorResponse);
        }

        @Test
        void should_get_contract_negotiations() {

            var ContractNegotiationList = contractNegotiations.request(shouldGetContractNegotiationsQuery());
            assertThat(ContractNegotiationList)
                    .satisfies(ContractNegotiationsTest.this::shouldGetContractNegotiationsResponse);
        }

        @Test
        void should_not_get_contract_negotiations() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = contractNegotiations.request(input);
            assertThat(result).satisfies(ContractNegotiationsTest.this::errorResponse);
        }

        @Test
        void should_get_a_contract_negotiation_state() {

            var state = contractNegotiations.getState("negotiation-id");

            assertThat(state.isSucceeded()).isTrue();
            assertThat(state.getContent()).isNotBlank();
        }

        @Test
        void should_not_get_a_contract_negotiation_state() {

            var state = contractNegotiations.getState("");
            assertThat(state).satisfies(ContractNegotiationsTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_contract_negotiation_async() {
            var contractNegotiation = contractNegotiations.getAsync("negotiation-id");
            assertThat(contractNegotiation)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest::shouldGetAContractNegotiationResponse);
        }

        @Test
        void should_not_get_a_contract_negotiation_when_id_is_empty_async() {
            var result = contractNegotiations.getAsync("");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::errorResponse);
        }

        @Test
        void should_create_a_contract_negotiation_async() {

            var result = contractNegotiations.createAsync(shouldCreateAContractNegotiationRequest());
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        @Disabled
        void should_get_a_contract_negotiation_attached_agreement_async() {
            var contractAgreement = contractNegotiations.getAgreementAsync("negotiation-id");
            assertThat(contractAgreement)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest::shouldGetAContractAgreementResponse);
        }

        @Test
        void should_not_get_a_contract_negotiation_attached_agreement_async() {
            var contractAgreement = contractNegotiations.getAgreementAsync("");
            assertThat(contractAgreement)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::errorResponse);
        }

        @Test
        void should_terminate_a_contract_negotiation_async() {

            var terminated = contractNegotiations.terminateAsync(shouldTerminateAContractNegotiationRequest());
            assertThat(terminated).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_terminate_a_contract_negotiation_when_id_is_empty_async() {

            var terminated = contractNegotiations.terminateAsync(shouldNotTerminateAContractNegotiationRequest());
            assertThat(terminated)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::errorResponse);
        }

        @Test
        void should_get_contract_negotiations_async() {

            var ContractNegotiationList = contractNegotiations.requestAsync(shouldGetContractNegotiationsQuery());
            assertThat(ContractNegotiationList)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::shouldGetContractNegotiationsResponse);
        }

        @Test
        void should_not_get_contract_negotiations_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = contractNegotiations.requestAsync(input);
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::errorResponse);
        }

        @Test
        void should_get_a_contract_negotiation_state_async() {

            var result = contractNegotiations.getStateAsync("negotiation-id");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(state -> {
                assertThat(state.isSucceeded()).isTrue();
                assertThat(state.getContent()).isNotBlank();
            });
        }

        @Test
        void should_not_get_a_contract_negotiation_state_async() {
            var state = contractNegotiations.getStateAsync("");
            assertThat(state)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::errorResponse);
        }
    }

    private <T> void errorResponse(Result<T> error) {
        assertThat(error.isSucceeded()).isFalse();
        assertThat(error.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isNotBlank();
            assertThat(apiErrorDetail.type()).isNotBlank();
            assertThat(apiErrorDetail.path()).isNotBlank();
            assertThat(apiErrorDetail.invalidValue()).isNotBlank();
        });
    }

    protected static void shouldGetAContractNegotiationResponse(Result<ContractNegotiation> contractNegotiation) {

        assertThat(contractNegotiation.isSucceeded()).isTrue();
        assertThat(contractNegotiation.getContent().id()).isNotBlank();
        assertThat(contractNegotiation.getContent().type()).isNotNull().satisfies(type -> assertThat(type)
                .isNotBlank());
        assertThat(contractNegotiation.getContent().protocol()).isNotNull().satisfies(protocol -> assertThat(protocol)
                .isNotBlank());
        assertThat(contractNegotiation.getContent().counterPartyId())
                .isNotNull()
                .satisfies(counterPartyId -> assertThat(counterPartyId).isNotBlank());
        assertThat(contractNegotiation.getContent().counterPartyAddress())
                .isNotNull()
                .satisfies(
                        counterPartyAddress -> assertThat(counterPartyAddress).isNotBlank());
        assertThat(contractNegotiation.getContent().state()).isNotNull().satisfies(state -> assertThat(state)
                .isNotBlank());
        assertThat(contractNegotiation.getContent().contractAgreementId())
                .isNotNull()
                .isNotBlank();
        assertThat(contractNegotiation.getContent().errorDetail())
                .isNotNull()
                .satisfies(errorDetail -> assertThat(errorDetail).isNotBlank());
        assertThat(contractNegotiation.getContent().callbackAddresses())
                .isNotNull()
                .first()
                .satisfies(callbackAddress -> {
                    assertThat(callbackAddress.authCodeId()).isNotNull().satisfies(authCodeId -> assertThat(authCodeId)
                            .isNotBlank());
                    assertThat(callbackAddress.authKey()).isNotNull().satisfies(authKey -> assertThat(authKey)
                            .isNotBlank());
                    assertThat(callbackAddress.transactional()).isNotNull().satisfies(transactional -> assertThat(
                                    transactional)
                            .isNotNull());
                    assertThat(callbackAddress.uri()).isNotNull().satisfies(uri -> assertThat(uri)
                            .isNotBlank());
                    assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                        assertThat(uri.get(0)).isNotBlank();
                        assertThat(uri.get(1)).isNotBlank();
                    });
                });
        assertThat(contractNegotiation.getContent().createdAt()).isGreaterThan(-1);
    }

    private ContractRequest shouldCreateAContractNegotiationRequest() {
        if (V3.equals(managementVersion)) {
            // Return the original JsonLD-based object for V3 compatibility
            var policy = JsonLdPolicy.Builder.newInstance() // Assuming this is your legacy class
                    .id("offer-id")
                    .assigner("providerId")
                    .target("assetId")
                    .build();

            return JsonLdContractRequest.Builder.newInstance()
                    .counterPartyAddress("http://provider-address")
                    .protocol("dataspace-protocol-http")
                    .policy(policy)
                    .build();
        } else {
            // Return the new POJO for V4BETA
            var policy = PojoPolicy.Builder.newInstance()
                    .id("offer-id")
                    .assigner("providerId")
                    .target("assetId")
                    .build();

            return PojoContractRequest.Builder.newInstance()
                    .counterPartyAddress("http://provider-address")
                    .protocol("dataspace-protocol-http")
                    .policy(policy)
                    .build();
        }
    }

    private TerminateNegotiation shouldTerminateAContractNegotiationRequest() {
        return TerminateNegotiation.Builder.newInstance()
                .id("negotiation-id")
                .reason("a reason to terminate")
                .build();
    }

    private TerminateNegotiation shouldNotTerminateAContractNegotiationRequest() {
        return TerminateNegotiation.Builder.newInstance()
                .id("")
                .reason("a reason to terminate")
                .build();
    }

    private QuerySpec shouldGetContractNegotiationsQuery() {
        return QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
    }

    private void shouldGetContractNegotiationsResponse(Result<List<ContractNegotiation>> ContractNegotiationList) {

        assertThat(ContractNegotiationList.isSucceeded()).isTrue();
        assertThat(ContractNegotiationList.getContent()).isNotNull().first().satisfies(contractNegotiation -> {
            assertThat(contractNegotiation.id()).isNotBlank();
            assertThat(contractNegotiation.type()).isNotNull().satisfies(type -> assertThat(type)
                    .isNotBlank());
            assertThat(contractNegotiation.protocol()).isNotNull().satisfies(protocol -> assertThat(protocol)
                    .isNotBlank());
            assertThat(contractNegotiation.counterPartyId())
                    .isNotNull()
                    .satisfies(counterPartyId -> assertThat(counterPartyId).isNotBlank());
            assertThat(contractNegotiation.counterPartyAddress())
                    .isNotNull()
                    .satisfies(counterPartyAddress ->
                            assertThat(counterPartyAddress).isNotBlank());
            assertThat(contractNegotiation.state()).isNotNull().satisfies(state -> assertThat(state)
                    .isNotBlank());
            assertThat(contractNegotiation.contractAgreementId())
                    .isNotNull()
                    .satisfies(contractAgreementId ->
                            assertThat(contractAgreementId).isNotBlank());
            assertThat(contractNegotiation.errorDetail()).isNotNull().satisfies(errorDetail -> assertThat(errorDetail)
                    .isNotBlank());
            assertThat(contractNegotiation.callbackAddresses())
                    .isNotNull()
                    .first()
                    .satisfies(callbackAddress -> {
                        assertThat(callbackAddress.authCodeId())
                                .isNotNull()
                                .satisfies(authCodeId -> assertThat(authCodeId).isNotBlank());
                        assertThat(callbackAddress.authKey()).isNotNull().satisfies(authKey -> assertThat(authKey)
                                .isNotBlank());
                        assertThat(callbackAddress.transactional()).isNotNull().satisfies(transactional -> assertThat(
                                        transactional)
                                .isNotNull());
                        assertThat(callbackAddress.uri()).isNotNull().satisfies(uri -> assertThat(uri)
                                .isNotBlank());
                        assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                            assertThat(uri.get(0)).isNotBlank();
                            assertThat(uri.get(1)).isNotBlank();
                        });
                    });
            assertThat(contractNegotiation.createdAt()).isGreaterThan(-1);
        });
    }
}
