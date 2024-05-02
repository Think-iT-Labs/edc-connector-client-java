package io.thinkit.edc.client.connector;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ObservabilityContainerTestBase {
    @Container
    static final ManagementApiContainer prism;

    static final int timeout = 5;

    static {
        prism = new ManagementApiContainer("/observability-api.yml");
    }
}
