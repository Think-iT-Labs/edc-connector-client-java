package io.thinkit.edc.client.connector;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ObservabilityContainerTestBase {
    @Container
    protected static final ApiContainer prism;

    protected static final int timeout = 5;

    static {
        prism = new ApiContainer("/observability-api.yml");
    }
}
