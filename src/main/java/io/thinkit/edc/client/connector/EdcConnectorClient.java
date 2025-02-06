package io.thinkit.edc.client.connector;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.thinkit.edc.client.connector.resource.EdcResource;
import io.thinkit.edc.client.connector.services.ApplicationObservability;
import io.thinkit.edc.client.connector.services.Assets;
import io.thinkit.edc.client.connector.services.CatalogCache;
import io.thinkit.edc.client.connector.services.Catalogs;
import io.thinkit.edc.client.connector.services.ContractAgreements;
import io.thinkit.edc.client.connector.services.ContractDefinitions;
import io.thinkit.edc.client.connector.services.ContractNegotiations;
import io.thinkit.edc.client.connector.services.Dataplanes;
import io.thinkit.edc.client.connector.services.Did;
import io.thinkit.edc.client.connector.services.EdcApiHttpClient;
import io.thinkit.edc.client.connector.services.EdrCache;
import io.thinkit.edc.client.connector.services.KeyPairs;
import io.thinkit.edc.client.connector.services.Participants;
import io.thinkit.edc.client.connector.services.PolicyDefinitions;
import io.thinkit.edc.client.connector.services.Secrets;
import io.thinkit.edc.client.connector.services.TransferProcesses;
import io.thinkit.edc.client.connector.services.VerifiableCredentials;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class EdcConnectorClient {

    private String managementUrl;
    private String observabilityUrl;
    private String catalogCacheUrl;
    private String identityUrl;
    private HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private UnaryOperator<HttpRequest.Builder> interceptor = UnaryOperator.identity();
    private final Map<Class<? extends EdcResource>, ResourceCreator> resourceCreators = new HashMap<>();
    private EdcApiHttpClient edcClientHttpClient;

    public static EdcConnectorClient newInstance() {
        return newBuilder().build();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private EdcConnectorClient() {}

    public Assets assets() {
        return resource(Assets.class);
    }

    public PolicyDefinitions policyDefinitions() {
        return resource(PolicyDefinitions.class);
    }

    public ContractDefinitions contractDefinitions() {
        return resource(ContractDefinitions.class);
    }

    public ContractNegotiations contractNegotiations() {
        return resource(ContractNegotiations.class);
    }

    public ContractAgreements contractAgreements() {
        return resource(ContractAgreements.class);
    }

    public TransferProcesses transferProcesses() {
        return resource(TransferProcesses.class);
    }

    public Dataplanes dataplanes() {
        return resource(Dataplanes.class);
    }

    public ApplicationObservability applicationObservability() {
        return resource(ApplicationObservability.class);
    }

    public Catalogs catalogs() {
        return resource(Catalogs.class);
    }

    public EdrCache edrCache() {
        return resource(EdrCache.class);
    }

    public Secrets secrets() {
        return resource(Secrets.class);
    }

    public CatalogCache catalogCache() {
        return resource(CatalogCache.class);
    }

    public VerifiableCredentials verifiableCredentials() {
        return resource(VerifiableCredentials.class);
    }

    public Did did() {
        return resource(Did.class);
    }

    public KeyPairs keyPairs() {
        return resource(KeyPairs.class);
    }

    public Participants participants() {
        return resource(Participants.class);
    }

    public <T extends EdcResource> T resource(Class<T> resourceClass) {
        var resourceCreator = resourceCreators.get(resourceClass);
        if (resourceCreator == null) {
            throw new IllegalArgumentException("No resource creator of type %s is registered on the client"
                    .formatted(resourceClass.getSimpleName()));
        }
        var resource = resourceCreator.create(createContext());
        if (!resourceClass.isInstance(resource)) {
            throw new IllegalArgumentException("Resource %s cannot be cast to %s".formatted(resource, resourceClass));
        }
        return resourceClass.cast(resource);
    }

    private EdcClientContext createContext() {
        return new EdcClientContext(
                new EdcClientUrls(managementUrl, observabilityUrl, catalogCacheUrl, identityUrl),
                objectMapper,
                edcClientHttpClient);
    }

    public static class Builder {

        private final EdcConnectorClient client = new EdcConnectorClient();

        public Builder() {
            client.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        public Builder managementUrl(String managementUrl) {
            client.managementUrl = managementUrl;
            return this;
        }

        public Builder observabilityUrl(String observabilityUrl) {
            client.observabilityUrl = observabilityUrl;
            return this;
        }

        public Builder catalogCacheUrl(String catalogCacheUrl) {
            client.catalogCacheUrl = catalogCacheUrl;
            return this;
        }

        public Builder identityUrl(String identityUrl) {
            client.identityUrl = identityUrl;
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            client.httpClient = httpClient;
            return this;
        }

        public Builder interceptor(UnaryOperator<HttpRequest.Builder> interceptor) {
            client.interceptor = interceptor;
            return this;
        }

        /**
         * Register an additional {@link EdcResource}
         *
         * @param resourceClass the class of the concrete {@link EdcResource} implementation
         * @param resourceCreator a function that instantiates an `{@link EdcResource} when needed.
         * @return the {@link Builder}
         */
        public Builder with(Class<? extends EdcResource> resourceClass, ResourceCreator resourceCreator) {
            client.resourceCreators.put(resourceClass, resourceCreator);
            return this;
        }

        public EdcConnectorClient build() {
            client.edcClientHttpClient = new EdcApiHttpClient(client.httpClient, client.interceptor);
            with(Assets.class, Assets::new);
            with(PolicyDefinitions.class, PolicyDefinitions::new);
            with(ContractDefinitions.class, ContractDefinitions::new);
            with(ContractNegotiations.class, ContractNegotiations::new);
            with(ContractAgreements.class, ContractAgreements::new);
            with(TransferProcesses.class, TransferProcesses::new);
            with(Dataplanes.class, Dataplanes::new);
            with(Catalogs.class, Catalogs::new);
            with(EdrCache.class, EdrCache::new);
            with(Secrets.class, Secrets::new);
            with(ApplicationObservability.class, ApplicationObservability::new);
            with(CatalogCache.class, CatalogCache::new);
            with(VerifiableCredentials.class, VerifiableCredentials::new);
            with(Did.class, Did::new);
            with(KeyPairs.class, KeyPairs::new);
            with(Participants.class, Participants::new);
            return client;
        }
    }
}
