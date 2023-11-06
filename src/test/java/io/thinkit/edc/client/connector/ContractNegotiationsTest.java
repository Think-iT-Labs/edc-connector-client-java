package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class ContractNegotiationsTest {

    @Container
    private final ManagementApiContainer prism = new ManagementApiContainer();

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

    @Test
    void should_get_a_contract_Negotiation() {
        var contractDefinition = contractNegotiations.get("negotiation-id");

        assertThat(contractDefinition.isSucceeded()).isTrue();
        assertThat(contractDefinition.getContent().id()).isNotBlank();
        assertThat(contractDefinition.getContent().type()).isNotNull().satisfies(type -> assertThat(type)
                .isEqualTo("PROVIDER"));
        assertThat(contractDefinition.getContent().protocol()).isNotNull().satisfies(protocol -> assertThat(protocol)
                .isEqualTo("dataspace-protocol-http"));
        assertThat(contractDefinition.getContent().counterPartyId())
                .isNotNull()
                .satisfies(counterPartyId -> assertThat(counterPartyId).isEqualTo("counter-party-id"));
        assertThat(contractDefinition.getContent().counterPartyAddress())
                .isNotNull()
                .satisfies(counterPartyAddress ->
                        assertThat(counterPartyAddress).isEqualTo("http://counter/party/address"));
        assertThat(contractDefinition.getContent().state()).isNotNull().satisfies(state -> assertThat(state)
                .isEqualTo("VERIFIED"));
        assertThat(contractDefinition.getContent().contractAgreementId())
                .isNotNull()
                .satisfies(
                        contractAgreementId -> assertThat(contractAgreementId).isEqualTo("contract:agreement:id"));
        assertThat(contractDefinition.getContent().errorDetail())
                .isNotNull()
                .satisfies(errorDetail -> assertThat(errorDetail).isEqualTo("eventual-error-detail"));
        assertThat(contractDefinition.getContent().callbackAddresses())
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
        assertThat(contractDefinition.getContent().createdAt()).isGreaterThan(0);
    }

    @Test
    void should_not_get_a_contract_Negotiation_when_id_is_empty() {
        var contractDefinition = contractNegotiations.get("");

        assertThat(contractDefinition.isSucceeded()).isFalse();
        assertThat(contractDefinition.getError()).isNotNull();
    }
}
