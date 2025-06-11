package io.thinkit.edc.client.connector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class RealTimeConnectorApiTestBase {
    protected static final int timeout = 60;
    protected static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(120);
    private static final String DOCKER_IMAGE_NAME = "connector:test";

    private static final String GRADLE_WRAPPER = "gradlew"; // or "gradlew.bat" on Windows
    private static File buildRoot;

    /**
     * Ensure the Docker image is built before starting tests.
     * This runs the Gradle dockerBuild task to create the my-java-project:test image.
     */
    @BeforeAll
    static void ensureDockerImageIsBuilt() {
        try {
            File gradleRoot = findBuildRoot();
            String gradleCommand =
                    System.getProperty("os.name").toLowerCase().contains("windows") ? "gradlew.bat" : "./gradlew";

            ProcessBuilder processBuilder = new ProcessBuilder(gradleCommand, "dockerBuild")
                    .directory(gradleRoot)
                    .inheritIO(); // This will show Gradle output in test logs

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IllegalStateException(
                        "Failed to build Docker image. Gradle dockerBuild task exited with code: " + exitCode);
            }

            System.out.println("Docker image " + DOCKER_IMAGE_NAME + " built successfully");

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Failed to execute Gradle dockerBuild task", e);
        }
    }

    /**
     * Utility method to locate the Gradle project root.
     * Search for build root will be done only once and cached for subsequent calls.
     *
     * @return The Gradle project root directory.
     */
    public static File findBuildRoot() {
        if (buildRoot != null) {
            return buildRoot;
        }

        File canonicalFile;
        try {
            canonicalFile = new File(".").getCanonicalFile();
        } catch (IOException e) {
            throw new IllegalStateException("Could not resolve current directory.", e);
        }

        buildRoot = findBuildRoot(canonicalFile);
        if (buildRoot == null) {
            throw new IllegalStateException("Could not find " + GRADLE_WRAPPER + " in parent directories.");
        }

        return buildRoot;
    }

    private static File findBuildRoot(File path) {
        if (path == null) {
            return null;
        }

        File gradlew = new File(path, GRADLE_WRAPPER);
        if (gradlew.exists()) {
            return path;
        }

        File parent = path.getParentFile();
        if (parent != null) {
            return findBuildRoot(parent);
        }

        return null;
    }

    /**
     * Returns the absolute normalized Path to the 'connector' folder relative to the Gradle project root.
     * This is used for file system bindings and configuration.
     */
    public static Path getConnectorFolder() {
        File root = findBuildRoot();
        Path connectorPath = root.toPath().resolve("connector").toAbsolutePath().normalize();

        // Verify the connector directory exists
        if (!connectorPath.toFile().exists()) {
            throw new IllegalStateException("Connector directory does not exist: " + connectorPath);
        }

        return connectorPath;
    }

    /**
     * Verify that the required configuration file exists.
     */
    private static void validateConfigurationFile() {
        Path configFile = getConnectorFolder().resolve("conf/consumer-connector.config");
        if (!configFile.toFile().exists()) {
            throw new IllegalStateException("Configuration file not found: " + configFile);
        }
    }

    @Container
    protected static final GenericContainer<?> myContainer = new GenericContainer<>(DOCKER_IMAGE_NAME)
            .withExposedPorts(9191, 9192, 9193, 9194, 9291)
            .withEnv("EDC_FS_CONFIG", "/config/configuration.properties")
            .withFileSystemBind(
                    getConnectorFolder()
                            .resolve("conf/consumer-connector.config/configuration.properties")
                            .toString(),
                    "/config/configuration.properties")
            .withLogConsumer(frame -> System.out.print(frame.getUtf8String()))
            .waitingFor(Wait.forListeningPort().withStartupTimeout(STARTUP_TIMEOUT))
            .withStartupTimeout(STARTUP_TIMEOUT);

    // Static block to validate configuration before container starts
    static {
        validateConfigurationFile();
    }

    /**
     * Get service URL for a specific port.
     *
     * @param port The internal container port
     * @return The mapped URL to access the service
     */
    protected String getServiceUrl(int port) {
        if (!myContainer.isRunning()) {
            throw new IllegalStateException(
                    "Container is not running. Make sure the test is properly annotated with @Testcontainers");
        }
        return "http://127.0.0.1:" + myContainer.getMappedPort(port);
    }

    /**
     * Get the default service URL (port 9191).
     * Typically used for general API access.
     */
    protected String getDefaultUrl() {
        return getServiceUrl(9191) + "api";
    }

    /**
     * Get the control API URL (port 9192).
     * Used for connector control operations.
     */
    public String getControlUrl() {
        return getServiceUrl(9192) + "control";
    }

    /**
     * Get the management API URL (port 9193).
     * Used for management and administrative operations.
     */
    public String getManagementUrl() {
        return getServiceUrl(9193) + "/management";
    }

    /**
     * Get the protocol API URL (port 9194).
     * Used for protocol-specific communications.
     */
    public String getProtocolUrl() {
        return getServiceUrl(9194) + "protocol";
    }

    /**
     * Get the data plane API URL (port 9291).
     * Used for data transfer operations.
     */
    public String getDataPlaneUrl() {
        return getServiceUrl(9291) + "public";
    }

    /**
     * Utility method to check if the container is healthy and ready.
     * Can be used in test setup to ensure the container is fully operational.
     */
    protected boolean isContainerReady() {
        return myContainer.isRunning() && myContainer.isHealthy();
    }

    /**
     * Get all exposed port mappings as a formatted string.
     * Useful for debugging and logging.
     */
    protected String getPortMappings() {
        if (!myContainer.isRunning()) {
            return "Container is not running";
        }

        return String.format(
                "Port mappings - Default: %d, Control: %d, Management: %d, Protocol: %d, DataPlane: %d",
                myContainer.getMappedPort(9191),
                myContainer.getMappedPort(9192),
                myContainer.getMappedPort(9193),
                myContainer.getMappedPort(9194),
                myContainer.getMappedPort(9291));
    }
}
