package io.thinkit.edc.client.connector;

import static org.testcontainers.containers.BindMode.READ_WRITE;

import org.testcontainers.containers.GenericContainer;

public class ManagementApiContainer extends GenericContainer<ManagementApiContainer> {

    public ManagementApiContainer(String file) {
        super("stoplight/prism:5.4.0");
        this.withClasspathResourceMapping(file, file, READ_WRITE);
        this.withCommand("mock -h 0.0.0.0 " + file);
        this.withExposedPorts(4010);
        this.withLogConsumer(frame -> {
            if (!frame.getUtf8String().contains("[CLI]")) {
                System.out.print(frame.getUtf8String());
            }
        });
    }

    public String getUrl() {
        return "http://127.0.0.1:%s".formatted(getFirstMappedPort());
    }
}
