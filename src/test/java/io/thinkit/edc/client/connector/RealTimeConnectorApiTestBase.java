package io.thinkit.edc.client.connector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

public abstract class RealTimeConnectorApiTestBase {
    protected static final int timeout = 60;
    protected static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(120);
    private static final String DOCKER_IMAGE_NAME = "connector:test";
    private static final String GRADLE_WRAPPER = "gradlew";

    protected static GenericContainer<?> myContainer;

    @BeforeAll
    static void setUpContainer() {
        ensureDockerImageIsBuilt();
        myContainer = createContainer();
        myContainer.start();
    }

    @AfterAll
    static void tearDownContainer() {
        if (myContainer != null) {
            myContainer.stop();
        }
    }

    static void ensureDockerImageIsBuilt() {
        try {
            File gradleRoot = findBuildRoot();
            String gradleCommand = "./gradlew";

            System.out.println("Building Docker image from: " + gradleRoot.getAbsolutePath());

            ProcessBuilder processBuilder = new ProcessBuilder(gradleCommand, "dockerBuild", "--no-daemon")
                    .directory(gradleRoot)
                    .inheritIO();

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            System.out.println("Docker build exit code: " + exitCode);

            if (exitCode != 0) {
                throw new IllegalStateException(
                        "Failed to build Docker image. Gradle dockerBuild task exited with code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Failed to execute Gradle dockerBuild task", e);
        }
    }

    public static File findBuildRoot() {
        try {
            File canonicalFile = new File(".").getCanonicalFile();
            File root = findBuildRootRecursive(canonicalFile);
            if (root == null) {
                throw new IllegalStateException("Could not find " + GRADLE_WRAPPER + " in parent directories.");
            }
            return root;
        } catch (IOException e) {
            throw new IllegalStateException("Could not resolve current directory.", e);
        }
    }

    private static File findBuildRootRecursive(File path) {
        if (path == null) {
            return null;
        }

        File gradlew = new File(path, GRADLE_WRAPPER);
        if (gradlew.exists()) {
            return path;
        }

        File parent = path.getParentFile();
        if (parent != null) {
            return findBuildRootRecursive(parent);
        }

        return null;
    }

    public static Path getConnectorFolder() {
        File root = findBuildRoot();
        Path connectorPath = root.toPath().resolve("connector").toAbsolutePath().normalize();
        if (!connectorPath.toFile().exists()) {
            throw new IllegalStateException("Connector directory does not exist: " + connectorPath);
        }

        return connectorPath;
    }

    private static GenericContainer<?> createContainer() {
        Path configFile = getConnectorFolder().resolve("conf/consumer-connector.config");
        if (!configFile.toFile().exists()) {
            throw new IllegalStateException("Configuration file not found: " + configFile);
        }

        return new GenericContainer<>(DOCKER_IMAGE_NAME)
                .withExposedPorts(9193)
                .withEnv("EDC_FS_CONFIG", "/config/configuration.properties")
                .withCopyFileToContainer(
                        MountableFile.forHostPath(configFile.resolve("configuration.properties")),
                        "/config/configuration.properties")
                .withLogConsumer(frame -> System.out.print(frame.getUtf8String()))
                .waitingFor(Wait.forHealthcheck().withStartupTimeout(STARTUP_TIMEOUT))
                .withStartupTimeout(STARTUP_TIMEOUT);
    }

    protected String getServiceUrl(int port) {
        if (myContainer == null || !myContainer.isRunning()) {
            throw new IllegalStateException(
                    "Container is not running. Make sure the test base class is properly initialized");
        }
        return "http://127.0.0.1:" + myContainer.getMappedPort(port);
    }

    public String getManagementUrl() {
        return getServiceUrl(9193) + "/management";
    }
}
