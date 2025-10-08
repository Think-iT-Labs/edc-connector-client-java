package io.thinkit.edc.client.connector.services;

import static org.assertj.core.api.Assertions.assertThat;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.*;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TransferProcessesTest extends ManagementApiTestBase {

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

    @Nested
    class Sync {
        @Test
        void should_get_a_transfer_process() {
            var transferProcess = transferProcesses.get("process-id");
            assertThat(transferProcess).satisfies(TransferProcessesTest.this::shouldGetATransferProcessResponse);
        }

        @Test
        void should_not_get_a_transfer_process_when_id_is_empty() {
            var transferProcess = transferProcesses.get("");
            assertThat(transferProcess).satisfies(TransferProcessesTest.this::errorResponse);

            errorResponse(transferProcess);
        }

        @Test
        void should_create_a_transfer_process() {

            var created = transferProcesses.create(shouldCreateATransferProcessRequest());

            assertThat(created.isSucceeded()).isTrue();
            assertThat(created.getContent()).isNotNull();
        }

        @Test
        void should_not_create_a_transfer_process_when_data_destination_is_empty() {

            var created = transferProcesses.create(shouldNotCreateATransferProcessRequest());
            assertThat(created).satisfies(TransferProcessesTest.this::errorResponse);
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
            assertThat(state).satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_terminate_a_transfer_process() {
            var terminated = transferProcesses.terminate(terminateATransferProcessRequest("process-id"));

            assertThat(terminated.isSucceeded()).isTrue();
            assertThat(terminated.getContent()).isNotNull();
        }

        @Test
        void should_not_terminate_a_transfer_process_when_id_is_empty() {

            var terminated = transferProcesses.terminate(terminateATransferProcessRequest(""));
            assertThat(terminated).satisfies(TransferProcessesTest.this::errorResponse);
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
            assertThat(result).satisfies(TransferProcessesTest.this::errorResponse);
        }
    }

    @Nested
    class Async {
        @Test
        void should_get_a_transfer_process_async() {
            var transferProcess = transferProcesses.getAsync("process-id");
            assertThat(transferProcess)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::shouldGetATransferProcessResponse);
        }

        @Test
        void should_not_get_a_transfer_process_when_id_is_empty_async() {
            var transferProcess = transferProcesses.getAsync("");

            assertThat(transferProcess)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_create_a_transfer_process_async() {
            var result = transferProcesses.createAsync(shouldCreateATransferProcessRequest());

            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(created -> {
                assertThat(created.isSucceeded()).isTrue();
                assertThat(created.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_create_a_transfer_process_when_data_destination_is_empty_async() {
            var created = transferProcesses.createAsync(shouldNotCreateATransferProcessRequest());
            assertThat(created)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_get_a_transfer_process_state_async() {
            var result = transferProcesses.getStateAsync("process-id");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(state -> {
                assertThat(state.isSucceeded()).isTrue();
                assertThat(state.getContent().state()).isNotNull().isEqualTo("STARTED");
            });
        }

        @Test
        void should_not_get_a_transfer_process_state_when_id_is_empty_async() {
            var state = transferProcesses.getStateAsync("");
            assertThat(state)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_terminate_a_transfer_process_async() {

            var result = transferProcesses.terminateAsync(terminateATransferProcessRequest("process-id"));
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(terminated -> {
                assertThat(terminated.isSucceeded()).isTrue();
                assertThat(terminated.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_terminate_a_transfer_process_when_id_is_empty_async() {

            var terminated = transferProcesses.terminateAsync(terminateATransferProcessRequest(""));
            assertThat(terminated)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_request_to_deprovision_the_transfer_process_async() {
            var result = transferProcesses.deprovisionAsync("process-id");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(deprovision -> {
                assertThat(deprovision.isSucceeded()).isTrue();
                assertThat(deprovision.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_request_to_deprovision_the_transfer_process_when_id_is_empty_async() {
            var result = transferProcesses.deprovisionAsync("");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
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

    private void shouldGetATransferProcessResponse(Result<TransferProcess> transferProcess) {
        assertThat(transferProcess.isSucceeded()).isTrue();
        assertThat(transferProcess.getContent().id()).isNotBlank();
        assertThat(transferProcess.getContent().correlationId()).isNotNull().isEqualTo("correlation-id");
        assertThat(transferProcess.getContent().type()).isNotNull().isEqualTo("PROVIDER");
        assertThat(transferProcess.getContent().state()).isNotNull().isEqualTo("STARTED");
        assertThat(transferProcess.getContent().stateTimestamp()).isGreaterThan(0);
        assertThat(transferProcess.getContent().assetId()).isNotNull().isEqualTo("asset-id");
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

    private TransferRequest shouldCreateATransferProcessRequest() {
        var privateProperties = Map.of("private-key", "data-destination-type");
        var dataDestination = Map.of("type", "data-destination-type");
        var callbackAddresses = CallbackAddress.Builder.newInstance()
                .transactional(false)
                .uri("http://callback/url")
                .authKey("auth-key")
                .authCodeId("auth-code-id")
                .events(Arrays.asList("contract.negotiation", "transfer.process"))
                .build();
        return TransferRequest.Builder.newInstance()
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .connectorId("provider-id")
                .contractId("contract-id")
                .assetId("asset-id")
                .dataDestination(dataDestination)
                .privateProperties(privateProperties)
                .callbackAddresses(List.of(callbackAddresses, callbackAddresses))
                .transferType("transferType")
                .build();
    }

    private TransferRequest shouldNotCreateATransferProcessRequest() {
        var privateProperties = Map.of("private-key", "data-destination-type");
        return TransferRequest.Builder.newInstance()
                .protocol("dataspace-protocol-http")
                .counterPartyAddress("http://provider-address")
                .connectorId("provider-id")
                .contractId("contract-id")
                .assetId("asset-id")
                .privateProperties(privateProperties)
                .build();
    }

    private TerminateTransfer terminateATransferProcessRequest(String id) {
        return TerminateTransfer.Builder.newInstance()
                .id(id)
                .reason("a reason to terminate")
                .build();
    }
}
