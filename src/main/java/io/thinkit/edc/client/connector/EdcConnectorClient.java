package io.thinkit.edc.client.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.thinkit.edc.client.connector.services.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.function.UnaryOperator;

public class EdcConnectorClient {

    private String managementUrl;
    private String observabilityUrl;
    private String catalogCacheUrl;
    private String identityUrl;
    private HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private UnaryOperator<HttpRequest.Builder> interceptor = UnaryOperator.identity();

    public static EdcConnectorClient newInstance() {
        return newBuilder().build();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private EdcConnectorClient() {}

    public Assets assets() {
        if (managementUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate Assets client without the management url");
        }
        return new Assets(managementUrl, httpClient, interceptor);
    }

    public PolicyDefinitions policyDefinitions() {
        if (managementUrl == null) {
            throw new IllegalArgumentException(
                    "Cannot instantiate PolicyDefinitions client without the management url");
        }
        return new PolicyDefinitions(managementUrl, httpClient, interceptor);
    }

    public ContractDefinitions contractDefinitions() {
        if (managementUrl == null) {
            throw new IllegalArgumentException(
                    "Cannot instantiate ContractDefinitions client without the management url");
        }
        return new ContractDefinitions(managementUrl, httpClient, interceptor);
    }

    public ContractNegotiations contractNegotiations() {
        if (managementUrl == null) {
            throw new IllegalArgumentException(
                    "Cannot instantiate ContractNegotiations client without the management url");
        }
        return new ContractNegotiations(managementUrl, httpClient, interceptor);
    }

    public ContractAgreements contractAgreements() {
        if (managementUrl == null) {
            throw new IllegalArgumentException(
                    "Cannot instantiate ContractAgreements client without the management url");
        }
        return new ContractAgreements(managementUrl, httpClient, interceptor);
    }

    public TransferProcesses transferProcesses() {
        if (managementUrl == null) {
            throw new IllegalArgumentException(
                    "Cannot instantiate transferProcesses client without the management url");
        }
        return new TransferProcesses(managementUrl, httpClient, interceptor);
    }

    public Dataplanes dataplanes() {
        if (managementUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate dataplanes client without the management url");
        }
        return new Dataplanes(managementUrl, httpClient, interceptor);
    }

    public ApplicationObservability applicationObservability() {
        if (observabilityUrl == null) {
            throw new IllegalArgumentException(
                    "Cannot instantiate ApplicationObservability client without the observability url");
        }
        return new ApplicationObservability(observabilityUrl, httpClient, interceptor);
    }

    public Catalogs catalogs() {
        if (managementUrl == null) {
            throw new IllegalArgumentException(
                    "Cannot instantiate ApplicationObservability client without the management url");
        }
        return new Catalogs(managementUrl, httpClient, interceptor);
    }

    public EdrCache edrCache() {
        if (managementUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate EdrCache client without the management url");
        }
        return new EdrCache(managementUrl, httpClient, interceptor);
    }

    public Secrets secrets() {
        if (managementUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate Secrets client without the management url");
        }
        return new Secrets(managementUrl, httpClient, interceptor);
    }

    public CatalogCache catalogCache() {
        if (catalogCacheUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate CatalogCache client without the catalog cache url");
        }
        return new CatalogCache(catalogCacheUrl, httpClient, interceptor);
    }

    public VerifiableCredentials verifiableCredentials() {
        if (identityUrl == null) {
            throw new IllegalArgumentException(
                    "Cannot instantiate verifiableCredentials client without the identity url");
        }
        return new VerifiableCredentials(identityUrl, httpClient, interceptor, objectMapper);
    }

    public Did did() {
        if (identityUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate Did client without the identity url");
        }
        return new Did(identityUrl, httpClient, interceptor, objectMapper);
    }

    public KeyPairs keyPairs() {
        if (identityUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate Did client without the identity url");
        }
        return new KeyPairs(identityUrl, httpClient, interceptor, objectMapper);
    }

    public static class Builder {

        private final EdcConnectorClient client = new EdcConnectorClient();

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

        public EdcConnectorClient build() {
            return client;
        }
    }
}
