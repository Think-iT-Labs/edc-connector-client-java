package io.thinkit.edc.client.connector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

public abstract class RealTimeConnectorApiTestBase {
    protected static final int timeout = 60;
    protected static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(120);
    private static final String DOCKER_IMAGE_NAME = "connector:test";
    private static final String GRADLE_WRAPPER = "gradlew";

    private static Network network;
    protected static GenericContainer<?> providerContainer;
    protected static GenericContainer<?> consumerContainer;

    @BeforeAll
    static void setUpContainers() {
        ensureDockerImageIsBuilt();
        network = Network.newNetwork();
        providerContainer = createContainer("provider-connector.config", "provider-connector");
        consumerContainer = createContainer("consumer-connector.config", "consumer-connector");
        providerContainer.start();
        consumerContainer.start();
    }

    @AfterAll
    static void tearDownContainers() {
        if (consumerContainer != null) {
            consumerContainer.stop();
        }
        if (providerContainer != null) {
            providerContainer.stop();
        }
        if (network != null) {
            network.close();
        }
    }

    static void ensureDockerImageIsBuilt() {
        try {
            var gradleRoot = findBuildRoot();
            var processBuilder = new ProcessBuilder("./gradlew", "dockerBuild", "--no-daemon")
                    .directory(gradleRoot)
                    .inheritIO();
            var exitCode = processBuilder.start().waitFor();
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
            var canonicalFile = new File(".").getCanonicalFile();
            var root = findBuildRootRecursive(canonicalFile);
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
        if (new File(path, GRADLE_WRAPPER).exists()) {
            return path;
        }
        return findBuildRootRecursive(path.getParentFile());
    }

    public static Path getConnectorFolder() {
        var root = findBuildRoot();
        var connectorPath = root.toPath().resolve("connector").toAbsolutePath().normalize();
        if (!connectorPath.toFile().exists()) {
            throw new IllegalStateException("Connector directory does not exist: " + connectorPath);
        }
        return connectorPath;
    }

    private static GenericContainer<?> createContainer(String configDirName, String networkAlias) {
        var configDir = getConnectorFolder().resolve("conf/" + configDirName);
        if (!configDir.toFile().exists()) {
            throw new IllegalStateException("Configuration directory not found: " + configDir);
        }

        return new GenericContainer<>(DOCKER_IMAGE_NAME)
                .withNetwork(network)
                .withNetworkAliases(networkAlias)
                .withExposedPorts(9191, 9192, 9193, 9194, 9291, 9393)
                .withEnv("EDC_FS_CONFIG", "/config/configuration.properties")
                .withCopyFileToContainer(
                        MountableFile.forHostPath(configDir.resolve("configuration.properties")),
                        "/config/configuration.properties")
                .withLogConsumer(frame -> System.out.print("[" + networkAlias + "] " + frame.getUtf8String()))
                .waitingFor(Wait.forHttp("/api/check/health")
                        .forPort(9191)
                        .forStatusCode(200)
                        .withStartupTimeout(STARTUP_TIMEOUT))
                .withStartupTimeout(STARTUP_TIMEOUT);
    }

    protected String getServiceUrl(GenericContainer<?> container, int port) {
        if (container == null || !container.isRunning()) {
            throw new IllegalStateException("Container is not running.");
        }
        return "http://127.0.0.1:" + container.getMappedPort(port);
    }

    public String getProviderManagementUrl() {
        return getServiceUrl(providerContainer, 9193) + "/management";
    }

    public String getConsumerManagementUrl() {
        return getServiceUrl(consumerContainer, 9193) + "/management";
    }

    public String getConsumerCatalogCacheUrl() {
        return getServiceUrl(consumerContainer, 9393) + "/catalog";
    }
}
