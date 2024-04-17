package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.ContractAgreementsTest.should_get_a_contract_agreement_response;
import static jakarta.json.Json.createObjectBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.services.ContractNegotiations;
import jakarta.json.Json;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ContractNegotiationsTest extends ContainerTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private ContractNegotiations contractNegotiations;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        contractNegotiations = client.contractNegotiations();
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

    protected static void should_get_a_contract_negotiation_response(Result<ContractNegotiation> contractNegotiation) {

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

    ContractRequest should_create_a_contract_negotiation_request() {
        var permissions = Json.createArrayBuilder()
                .add(createObjectBuilder().add("target", "asset-id").add("action", "display"))
                .build();

        var policy = Policy.Builder.newInstance()
                .raw(createObjectBuilder().add("permission", permissions).build())
                .build();
        var callbackAddresses = CallbackAddress.Builder.newInstance()
                .transactional(false)
                .uri("http://callback/url")
                .authKey("auth-key")
                .authCodeId("auth-code-id")
                .events(Arrays.asList("contract.negotiation", "transfer.process"))
                .build();
        return ContractRequest.Builder.newInstance()
                .counterPartyAddress("http://provider-address")
                .protocol("dataspace-protocol-http")
                .policy(policy)
                .callbackAddresses(List.of(callbackAddresses, callbackAddresses))
                .build();
    }

    TerminateNegotiation should_terminate_a_contract_negotiation_request() {
        return TerminateNegotiation.Builder.newInstance()
                .id("negotiation-id")
                .reason("a reason to terminate")
                .build();
    }

    TerminateNegotiation should_not_terminate_a_contract_negotiation_request() {
        return TerminateNegotiation.Builder.newInstance()
                .id("")
                .reason("a reason to terminate")
                .build();
    }

    QuerySpec should_get_contract_negotiations_query() {
        return QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
    }

    void should_get_contract_negotiations_response(Result<List<ContractNegotiation>> ContractNegotiationList) {

        assertThat(ContractNegotiationList.isSucceeded()).isTrue();
        assertThat(ContractNegotiationList.getContent()).isNotNull().first().satisfies(contractNegotiation -> {
            assertThat(contractNegotiation.id()).isNotBlank();
            assertThat(contractNegotiation.type()).isNotNull().satisfies(type -> assertThat(type)
                    .isEqualTo("PROVIDER"));
            assertThat(contractNegotiation.protocol()).isNotNull().satisfies(protocol -> assertThat(protocol)
                    .isEqualTo("dataspace-protocol-http"));
            assertThat(contractNegotiation.counterPartyId())
                    .isNotNull()
                    .satisfies(counterPartyId -> assertThat(counterPartyId).isEqualTo("counter-party-id"));
            assertThat(contractNegotiation.counterPartyAddress())
                    .isNotNull()
                    .satisfies(counterPartyAddress ->
                            assertThat(counterPartyAddress).isEqualTo("http://counter/party/address"));
            assertThat(contractNegotiation.state()).isNotNull().satisfies(state -> assertThat(state)
                    .isEqualTo("VERIFIED"));
            assertThat(contractNegotiation.contractAgreementId())
                    .isNotNull()
                    .satisfies(contractAgreementId ->
                            assertThat(contractAgreementId).isEqualTo("contract:agreement:id"));
            assertThat(contractNegotiation.errorDetail()).isNotNull().satisfies(errorDetail -> assertThat(errorDetail)
                    .isEqualTo("eventual-error-detail"));
            assertThat(contractNegotiation.callbackAddresses())
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
            assertThat(contractNegotiation.createdAt()).isGreaterThan(0);
        });
    }

    @Nested
    class Sync {
        @Test
        void should_get_a_contract_negotiation() {
            var contractNegotiation = contractNegotiations.get("negotiation-id");
            should_get_a_contract_negotiation_response(contractNegotiation);
        }

        @Test
        void should_not_get_a_contract_negotiation_when_id_is_empty() {
            var contractNegotiation = contractNegotiations.get("");
            error_response(contractNegotiation);
        }

        @Test
        void should_create_a_contract_negotiation() {
            var created = contractNegotiations.create(should_create_a_contract_negotiation_request());
            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_get_a_contract_negotiation_attached_agreement() {
            var contractAgreement = contractNegotiations.getAgreement("negotiation-id");
            should_get_a_contract_agreement_response(contractAgreement);
        }

        @Test
        void should_not_get_a_contract_negotiation_attached_agreement() {
            var contractAgreement = contractNegotiations.getAgreement("");
            error_response(contractAgreement);
        }

        @Test
        void should_terminate_a_contract_negotiation() {
            var terminated = contractNegotiations.terminate(should_terminate_a_contract_negotiation_request());
            assertThat(terminated.isSucceeded()).isTrue();
            assertThat(terminated.getContent()).isNotNull();
        }

        @Test
        void should_not_terminate_a_contract_negotiation_when_id_is_empty() {

            var terminated = contractNegotiations.terminate(should_not_terminate_a_contract_negotiation_request());
            error_response(terminated);
        }

        @Test
        void should_get_contract_negotiations() {

            var ContractNegotiationList = contractNegotiations.request(should_get_contract_negotiations_query());
            should_get_contract_negotiations_response(ContractNegotiationList);
        }

        @Test
        void should_not_get_contract_negotiations() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = contractNegotiations.request(input);

            error_response(result);
        }

        @Test
        void should_get_a_contract_negotiation_state() {

            var state = contractNegotiations.getState("negotiation-id");

            assertThat(state.isSucceeded()).isTrue();
            assertThat(state.getContent()).isNotNull().isEqualTo("string");
        }

        @Test
        void should_not_get_a_contract_negotiation_state() {

            var state = contractNegotiations.getState("");

            error_response(state);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_contract_negotiation_async() {
            var contractNegotiation = contractNegotiations.getAsync("negotiation-id");
            assertThat(contractNegotiation)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest::should_get_a_contract_negotiation_response);
        }

        @Test
        void should_not_get_a_contract_negotiation_when_id_is_empty_async() {
            var result = contractNegotiations.getAsync("");
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::error_response);
        }

        @Test
        void should_create_a_contract_negotiation_async() {

            var result = contractNegotiations.createAsync(should_create_a_contract_negotiation_request());
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_get_a_contract_negotiation_attached_agreement_async() {
            var contractAgreement = contractNegotiations.getAgreementAsync("negotiation-id");
            assertThat(contractAgreement)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractAgreementsTest::should_get_a_contract_agreement_response);
        }

        @Test
        void should_not_get_a_contract_negotiation_attached_agreement_async() {
            var contractAgreement = contractNegotiations.getAgreementAsync("");
            assertThat(contractAgreement)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::error_response);
        }

        @Test
        void should_terminate_a_contract_negotiation_async() {

            var terminated = contractNegotiations.terminateAsync(should_terminate_a_contract_negotiation_request());
            assertThat(terminated).succeedsWithin(5, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_terminate_a_contract_negotiation_when_id_is_empty_async() {

            var terminated = contractNegotiations.terminateAsync(should_not_terminate_a_contract_negotiation_request());
            assertThat(terminated)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::error_response);
        }

        @Test
        void should_get_contract_negotiations_async() {

            var ContractNegotiationList = contractNegotiations.requestAsync(should_get_contract_negotiations_query());
            assertThat(ContractNegotiationList)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::should_get_contract_negotiations_response);
        }

        @Test
        void should_not_get_contract_negotiations_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = contractNegotiations.requestAsync(input);
            assertThat(result)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::error_response);
        }

        @Test
        void should_get_a_contract_negotiation_state_async() {

            var result = contractNegotiations.getStateAsync("negotiation-id");
            assertThat(result).succeedsWithin(5, TimeUnit.SECONDS).satisfies(state -> {
                assertThat(state.isSucceeded()).isTrue();
                assertThat(state.getContent()).isNotNull().isEqualTo("string");
            });
        }

        @Test
        void should_not_get_a_contract_negotiation_state_async() {
            var state = contractNegotiations.getStateAsync("");
            assertThat(state)
                    .succeedsWithin(5, TimeUnit.SECONDS)
                    .satisfies(ContractNegotiationsTest.this::error_response);
        }
    }
}
