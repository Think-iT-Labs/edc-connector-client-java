package io.thinkit.edc.client.connector;

import io.thinkit.edc.client.connector.services.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.function.UnaryOperator;

public class EdcConnectorClient {

    private String managementUrl;
    private String observabilityUrl;
    private HttpClient httpClient = HttpClient.newHttpClient();
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
