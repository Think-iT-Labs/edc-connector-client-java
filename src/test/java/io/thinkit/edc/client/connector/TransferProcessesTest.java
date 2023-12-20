package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.model.CallbackAddress;
import io.thinkit.edc.client.connector.model.TerminateTransfer;
import io.thinkit.edc.client.connector.model.TransferRequest;
import io.thinkit.edc.client.connector.services.TransferProcesses;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class TransferProcessesTest extends ContainerBaseTest {

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
        assertThat(transferProcess.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_create_a_transfer_process() {
        var privateProperties = Map.of("private-key", "data-destination-type");
        var dataDestination = Map.of("type", "data-destination-type");
        var callbackAddresses = CallbackAddress.Builder.newInstance()
                .transactional(false)
                .uri("http://callback/url")
                .authKey("auth-key")
                .authCodeId("auth-code-id")
                .events(Arrays.asList("contract.negotiation", "transfer.process"))
                .build();
        var transferRequest = TransferRequest.Builder.newInstance()
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .connectorId("provider-id")
                .contractId("contract-id")
                .assetId("asset-id")
                .dataDestination(dataDestination)
                .privateProperties(privateProperties)
                .callbackAddresses(List.of(callbackAddresses, callbackAddresses))
                .build();

        var created = transferProcesses.create(transferRequest);

        assertThat(created.isSucceeded()).isTrue();
        assertThat(created.getContent()).isNotNull();
    }

    @Test
    void should_not_create_a_transfer_process_when_data_destination_is_empty() {
        var privateProperties = Map.of("private-key", "data-destination-type");
        var transferRequest = TransferRequest.Builder.newInstance()
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .connectorId("provider-id")
                .contractId("contract-id")
                .assetId("asset-id")
                .privateProperties(privateProperties)
                .build();

        var created = transferProcesses.create(transferRequest);

        assertThat(created.isSucceeded()).isFalse();
        assertThat(created.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_get_a_transfer_process_state() {

        var state = transferProcesses.getState("process-id");

        assertThat(state.isSucceeded()).isTrue();
        assertThat(state.getContent().state()).isNotNull().isEqualTo("STARTED");
    }

    @Test
    void should_not_get_a_transfer_process_state_when_id_is_empty() {

        var state = transferProcesses.getState("");

        assertThat(state.isSucceeded()).isFalse();
        assertThat(state.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_terminate_a_transfer_process() {
        var input = TerminateTransfer.Builder.newInstance()
                .id("process-id")
                .reason("a reason to terminate")
                .build();
        var terminated = transferProcesses.terminate(input);

        assertThat(terminated.isSucceeded()).isTrue();
        assertThat(terminated.getContent()).isNotNull();
    }

    @Test
    void should_not_terminate_a_transfer_process_when_id_is_empty() {
        var input = TerminateTransfer.Builder.newInstance()
                .id("")
                .reason("a reason to terminate")
                .build();
        var terminated = transferProcesses.terminate(input);

        assertThat(terminated.isSucceeded()).isFalse();
        assertThat(terminated.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }

    @Test
    void should_request_to_deprovision_the_transfer_process() {
        var result = transferProcesses.deprovision("process-id");

        assertThat(result.isSucceeded()).isTrue();
        assertThat(result.getContent()).isNotNull();
    }

    @Test
    void should_not_request_to_deprovision_the_transfer_process_when_id_is_empty() {
        var result = transferProcesses.deprovision("");

        assertThat(result.isSucceeded()).isFalse();
        assertThat(result.getErrors()).isNotNull().first().satisfies(apiErrorDetail -> {
            assertThat(apiErrorDetail.message()).isEqualTo("error message");
            assertThat(apiErrorDetail.type()).isEqualTo("ErrorType");
            assertThat(apiErrorDetail.path()).isEqualTo("object.error.path");
            assertThat(apiErrorDetail.invalidValue()).isEqualTo("this value is not valid");
        });
    }
}
