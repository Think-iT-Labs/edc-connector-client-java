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
import org.testcontainers.utility.MountableFile;

@Testcontainers
public abstract class RealTimeConnectorApiTestBase {
    protected static final int timeout = 60;
    protected static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(120);
    private static final String DOCKER_IMAGE_NAME = "connector:test";

    private static final String GRADLE_WRAPPER = "gradlew";
    private static File buildRoot;

    @BeforeAll
    static void ensureDockerImageIsBuilt() {
        if (!dockerImageExists()) {
            buildDockerImage();
        } else {
            System.out.println("Docker image already exists, skipping build");
        }
    }

    private static boolean dockerImageExists() {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "inspect", DOCKER_IMAGE_NAME).redirectErrorStream(true);
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private static void buildDockerImage() {
        try {
            File gradleRoot = findBuildRoot();
            String gradleCommand = "./gradlew";

            ProcessBuilder processBuilder = new ProcessBuilder(gradleCommand, "dockerBuild", "--no-daemon")
                    .directory(gradleRoot)
                    .inheritIO();

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IllegalStateException(
                        "Failed to build Docker image. Gradle dockerBuild task exited with code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Failed to execute Gradle dockerBuild task", e);
        }
    }

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

    public static Path getConnectorFolder() {
        File root = findBuildRoot();
        Path connectorPath = root.toPath().resolve("connector").toAbsolutePath().normalize();
        if (!connectorPath.toFile().exists()) {
            throw new IllegalStateException("Connector directory does not exist: " + connectorPath);
        }

        return connectorPath;
    }

    @Container
    protected static final GenericContainer<?> myContainer = createContainer();

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
                .waitingFor(Wait.forListeningPort().withStartupTimeout(STARTUP_TIMEOUT))
                .withStartupTimeout(STARTUP_TIMEOUT);
    }

    protected String getServiceUrl(int port) {
        if (!myContainer.isRunning()) {
            throw new IllegalStateException(
                    "Container is not running. Make sure the test is properly annotated with @Testcontainers");
        }
        return "http://127.0.0.1:" + myContainer.getMappedPort(port);
    }

    public String getManagementUrl() {
        return getServiceUrl(9193) + "/management";
    }
}
