package io.thinkit.edc.client.connector;

import org.testcontainers.junit.jupiter.Container;

public abstract class ContainerBaseTest {
    @Container
    static final ManagementApiContainer prism;

    static {
        prism = new ManagementApiContainer();
    }
}
