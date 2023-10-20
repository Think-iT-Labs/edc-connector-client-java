package io.thinkit.edc.client.connector;

import static org.testcontainers.containers.BindMode.READ_WRITE;

import org.testcontainers.containers.GenericContainer;

public class ManagementApiContainer extends GenericContainer<ManagementApiContainer> {

    public ManagementApiContainer() {
        super("stoplight/prism:5.4.0");
        this.withClasspathResourceMapping("/management-api.yml", "/management-api.yml", READ_WRITE);
        this.withCommand("mock -h 0.0.0.0 /management-api.yml");
        this.withExposedPorts(4010);
        this.withLogConsumer(frame -> {
            if (!frame.getUtf8String().contains("[CLI]")) {
                System.out.print(frame.getUtf8String());
            }
        });
    }
}
