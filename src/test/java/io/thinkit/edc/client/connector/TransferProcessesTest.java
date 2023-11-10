package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class TransferProcessesTest {

    @Container
    private final ManagementApiContainer prism = new ManagementApiContainer();

    private final HttpClient http = HttpClient.newBuilder().build();
    private TransferProcesses transferProcesses;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .managementUrl(prism.getUrl())
                .build();
        transferProcesses = client.transferProcesses();
    }

    @Test
    void should_get_a_transfer_process() {
        var transferProcess = transferProcesses.get("process-id");

        assertThat(transferProcess.isSucceeded()).isTrue();
        assertThat(transferProcess.getContent().id()).isNotBlank();
        assertThat(transferProcess.getContent().correlationId()).isNotNull().isEqualTo("correlation-id");
        assertThat(transferProcess.getContent().type()).isNotNull().isEqualTo("PROVIDER");
        assertThat(transferProcess.getContent().state()).isNotNull().isEqualTo("STARTED");
        assertThat(transferProcess.getContent().stateTimestamp()).isGreaterThan(0);
        assertThat(transferProcess.getContent().assetId()).isNotNull().isEqualTo("asset-id");
        assertThat(transferProcess.getContent().connectorId()).isNotNull().isEqualTo("connectorId");
        assertThat(transferProcess.getContent().contractId()).isNotNull().isEqualTo("contractId");
        assertThat(transferProcess.getContent().dataDestination()).isNotNull().satisfies(dataDestination -> {
            assertThat(dataDestination.size()).isGreaterThan(0);
            assertThat(dataDestination.getString("type")).isEqualTo("data-destination-type");
        });
        assertThat(transferProcess.getContent().privateProperties()).isNotNull().satisfies(privateProperties -> {
            assertThat(privateProperties.size()).isGreaterThan(0);
            assertThat(privateProperties.getString("private-key")).isEqualTo("private-value");
        });
        assertThat(transferProcess.getContent().errorDetail()).isNotNull().isEqualTo("eventual-error-detail");
        assertThat(transferProcess.getContent().callbackAddresses())
                .isNotNull()
                .first()
                .satisfies(callbackAddress -> {
                    assertThat(callbackAddress.authCodeId()).isNotNull().isEqualTo("auth-code-id");
                    assertThat(callbackAddress.authKey()).isNotNull().isEqualTo("auth-key");
                    assertThat(callbackAddress.transactional()).isNotNull().isFalse();
                    assertThat(callbackAddress.uri()).isNotNull().isEqualTo("http://callback/url");
                    assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                        assertThat(uri.get(0)).isEqualTo("contract.negotiation");
                        assertThat(uri.get(1)).isEqualTo("transfer.process");
                    });
                });
        assertThat(transferProcess.getContent().createdAt()).isGreaterThan(0);
    }

    @Test
    void should_not_get_a_transfer_process_when_id_is_empty() {
        var transferProcess = transferProcesses.get("");

        assertThat(transferProcess.isSucceeded()).isFalse();
        assertThat(transferProcess.getError()).isNotNull();
    }

    @Test
    void should_request_to_deprovision_the_transfer_process() {
        var result = transferProcesses.requestToDeprovision("process-id");

        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent()).isNotNull();
    }

    @Test
    void should_not_request_to_deprovision_the_transfer_process_when_id_is_empty() {
        var result = transferProcesses.requestToDeprovision("");

        assertThat(result.isSucceeded()).isFalse();
        assertThat(result.getError()).isNotNull();
    }
}
