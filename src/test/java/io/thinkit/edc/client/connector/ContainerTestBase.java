package io.thinkit.edc.client.connector;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ContainerTestBase {
    @Container
    static final ManagementApiContainer prism;

    static {
        prism = new ManagementApiContainer();
    }
}
