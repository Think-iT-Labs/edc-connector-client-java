package io.thinkit.edc.client.connector;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ObservabilityContainerTestBase {
    @Container
    static final ApiContainer prism;

    static final int timeout = 5;

    static {
        prism = new ApiContainer("/observability-api.yml");
    }
}
