package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
class ContractDefinitionsTest {

    @Container
    private GenericContainer<?> prism = new GenericContainer<>("stoplight/prism:3.3.4")
            .withFileSystemBind(new File("").getAbsolutePath(), "/tmp")
            .withCopyToContainer(MountableFile.forClasspathResource("/edcOpenAPi.yaml"), "/edcOpenAPi.yaml")
            .withCommand("mock -h 0.0.0.0 -d /edcOpenAPi.yaml")
            .withExposedPorts(4010)
            .withLogConsumer(frame -> {
                if (!frame.getUtf8String().contains("[CLI]")) {
                    System.out.println(frame.getUtf8String());
                }
            });

    private final HttpClient http = HttpClient.newBuilder().build();
    private ContractDefinitions contractDefinitions;

    @BeforeEach
    void setUp() {
        var client = EdcConnectorClient.newBuilder()
                .httpClient(http)
                .interceptor(r -> r.header("Prefer", "dynamic=false"))
                .managementUrl("http://127.0.0.1:%s".formatted(prism.getFirstMappedPort()))
                .build();
        contractDefinitions = client.contractDefinitions();
    }

    @Test
    void should_get_a_contract_definition() {
        Result<ContractDefinition> contractDefinition = contractDefinitions.get("definition-id");

        assertThat(contractDefinition.isSucceeded()).isTrue();
        assertThat(contractDefinition.getContent().id()).isNotBlank();
        assertThat(contractDefinition.getContent().accessPolicyId()).isNotNull().satisfies(accessPolicyId -> {
            assertThat(accessPolicyId).isEqualTo("asset-policy-id");
        });
        assertThat(contractDefinition.getContent().contractPolicyId())
                .isNotNull()
                .satisfies(contractPolicyId -> {
                    assertThat(contractPolicyId).isEqualTo("contract-policy-id");
                });
        assertThat(contractDefinition.getContent().createdAt()).isGreaterThan(0);
    }

    @Test
    void should_not_get_a_contract_definition_when_id_is_empty() {
        Result<ContractDefinition> contractDefinition = contractDefinitions.get("");

        assertThat(contractDefinition.isSucceeded()).isFalse();
        assertThat(contractDefinition.getError()).isNotNull();
    }
}
