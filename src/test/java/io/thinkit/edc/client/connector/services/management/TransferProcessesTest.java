package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V3;
import static io.thinkit.edc.client.connector.EdcConnectorClient.Versions.V4BETA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import io.thinkit.edc.client.connector.EdcConnectorClient;
import io.thinkit.edc.client.connector.ManagementApiTestBase;
import io.thinkit.edc.client.connector.model.*;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdCallbackAddress;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.ValueSource;

@ParameterizedClass
@ValueSource(strings = {V3, V4BETA})
class TransferProcessesTest extends ManagementApiTestBase {

    private final HttpClient http = HttpClient.newBuilder().build();
    private final String managementVersion;
    private TransferProcesses transferProcesses;

    TransferProcessesTest(String managementVersion) {
        this.managementVersion = managementVersion;
    }

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .management(prism.getUrl(), managementVersion)
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
            assertThat(state.getContent().state()).isNotNull().isNotBlank();
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
            assumeTrue(V3.equals(managementVersion), "Deprovisioning is only supported in V3");
            var result = transferProcesses.deprovision("process-id");

            assertThat(result.isSucceeded()).isTrue();
            assertThat(result.getContent()).isNotNull();
        }

        @Test
        void should_not_request_to_deprovision_the_transfer_process_when_id_is_empty() {
            assumeTrue(V3.equals(managementVersion), "Deprovisioning is only supported in V3");
            var result = transferProcesses.deprovision("");
            assertThat(result).satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_resume_a_transfer_process() {
            var resumed = transferProcesses.resume("process-id");

            assertThat(resumed.isSucceeded()).isTrue();
            assertThat(resumed.getContent()).isNotNull();
        }

        @Test
        void should_not_resume_a_transfer_process_when_id_is_empty() {

            var resumed = transferProcesses.resume("");
            assertThat(resumed).satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_suspend_a_transfer_process() {
            var suspended = transferProcesses.suspend(terminateATransferProcessRequest("process-id"));

            assertThat(suspended.isSucceeded()).isTrue();
            assertThat(suspended.getContent()).isNotNull();
        }

        @Test
        void should_not_suspend_a_transfer_process_when_id_is_empty() {

            var suspended = transferProcesses.suspend(terminateATransferProcessRequest(""));
            assertThat(suspended).satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_get_transfer_Processes() {

            var transferProcessList = transferProcesses.request(shouldGetTransferProcessesQuery());
            assertThat(transferProcessList).satisfies(TransferProcessesTest.this::shouldGetTransferProcessesResponse);
        }

        @Test
        void should_not_get_contract_negotiations() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = transferProcesses.request(input);
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
                assertThat(state.getContent().state()).isNotNull().isNotBlank();
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
            assumeTrue(V3.equals(managementVersion), "Deprovisioning is only supported in V3");
            var result = transferProcesses.deprovisionAsync("process-id");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(deprovision -> {
                assertThat(deprovision.isSucceeded()).isTrue();
                assertThat(deprovision.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_request_to_deprovision_the_transfer_process_when_id_is_empty_async() {
            assumeTrue(V3.equals(managementVersion), "Deprovisioning is only supported in V3");
            var result = transferProcesses.deprovisionAsync("");
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_resume_a_transfer_process_async() {

            var result = transferProcesses.resumeAsync("process-id");
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(resumed -> {
                assertThat(resumed.isSucceeded()).isTrue();
                assertThat(resumed.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_resume_a_transfer_process_when_id_is_empty_async() {

            var resumed = transferProcesses.resumeAsync("");
            assertThat(resumed)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_suspend_a_transfer_process_async() {

            var result = transferProcesses.suspendAsync(terminateATransferProcessRequest("process-id"));
            assertThat(result).succeedsWithin(timeout, TimeUnit.SECONDS).satisfies(suspended -> {
                assertThat(suspended.isSucceeded()).isTrue();
                assertThat(suspended.getContent()).isNotNull();
            });
        }

        @Test
        void should_not_suspend_a_transfer_process_when_id_is_empty_async() {

            var suspended = transferProcesses.suspendAsync(terminateATransferProcessRequest(""));
            assertThat(suspended)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
        }

        @Test
        void should_get_transfer_processes_async() {

            var result = transferProcesses.requestAsync(shouldGetTransferProcessesQuery());
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::shouldGetTransferProcessesResponse);
        }

        @Test
        void should_not_get_transfer_processes_async() {
            var input = QuerySpec.Builder.newInstance().sortOrder("wrong").build();

            var result = transferProcesses.requestAsync(input);
            assertThat(result)
                    .succeedsWithin(timeout, TimeUnit.SECONDS)
                    .satisfies(TransferProcessesTest.this::errorResponse);
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

    private void shouldGetATransferProcessResponse(Result<TransferProcess> transferProcess) {
        assertThat(transferProcess.isSucceeded()).isTrue();
        assertThat(transferProcess.getContent().id()).isNotBlank();
        assertThat(transferProcess.getContent().correlationId()).isNotNull().isNotBlank();
        assertThat(transferProcess.getContent().type()).isNotNull().isNotBlank();
        assertThat(transferProcess.getContent().state()).isNotNull().isNotBlank();
        assertThat(transferProcess.getContent().stateTimestamp()).isGreaterThan(-1);
        assertThat(transferProcess.getContent().assetId()).isNotNull().isNotBlank();
        assertThat(transferProcess.getContent().contractId()).isNotNull().isNotBlank();

        assertThat(transferProcess.getContent().errorDetail()).isNotNull().isNotBlank();
        assertThat(transferProcess.getContent().callbackAddresses())
                .isNotNull()
                .first()
                .satisfies(callbackAddress -> {
                    assertThat(callbackAddress.authCodeId()).isNotNull().isNotBlank();
                    assertThat(callbackAddress.authKey()).isNotNull().isNotBlank();
                    assertThat(callbackAddress.transactional()).isNotNull();
                    assertThat(callbackAddress.uri()).isNotNull().isNotBlank();
                    assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                        assertThat(uri.get(0)).isNotBlank();
                        assertThat(uri.get(1)).isNotBlank();
                    });
                });
        assertThat(transferProcess.getContent().createdAt()).isGreaterThan(-1);
    }

    private TransferRequest shouldCreateATransferProcessRequest() {
        var privateProperties = Map.of("private-key", "data-destination-type");
        var dataDestination = Map.of("type", "data-destination-type");
        var callbackAddresses = JsonLdCallbackAddress.Builder.newInstance()
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

    private QuerySpec shouldGetTransferProcessesQuery() {
        return QuerySpec.Builder.newInstance()
                .offset(5)
                .limit(10)
                .sortOrder("DESC")
                .sortField("fieldName")
                .build();
    }

    private void shouldGetTransferProcessesResponse(Result<List<TransferProcess>> transferProcessList) {

        assertThat(transferProcessList.isSucceeded()).isTrue();
        assertThat(transferProcessList.getContent()).isNotNull().first().satisfies(transferProcess -> {
            assertThat(transferProcess.id()).isNotBlank();
            assertThat(transferProcess.correlationId()).isNotNull().isNotBlank();
            assertThat(transferProcess.type()).isNotNull().isNotBlank();
            assertThat(transferProcess.state()).isNotNull().isNotBlank();
            assertThat(transferProcess.stateTimestamp()).isGreaterThan(-1);
            assertThat(transferProcess.assetId()).isNotNull().isNotBlank();
            assertThat(transferProcess.contractId()).isNotNull().isNotBlank();

            assertThat(transferProcess.errorDetail()).isNotNull().isNotBlank();
            assertThat(transferProcess.callbackAddresses()).isNotNull().first().satisfies(callbackAddress -> {
                assertThat(callbackAddress.authCodeId()).isNotNull().isNotBlank();
                assertThat(callbackAddress.authKey()).isNotNull().isNotBlank();
                assertThat(callbackAddress.transactional()).isNotNull();
                assertThat(callbackAddress.uri()).isNotNull().isNotBlank();
                assertThat(callbackAddress.events()).isNotNull().satisfies(uri -> {
                    assertThat(uri.get(0)).isNotBlank();
                    assertThat(uri.get(1)).isNotBlank();
                });
            });
            assertThat(transferProcess.createdAt()).isGreaterThan(-1);
        });
    }
}
