package io.thinkit.edc.client.connector;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.function.UnaryOperator;

public class EdcConnectorClient {

    private String managementUrl;
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

    public static class Builder {

        private final EdcConnectorClient client = new EdcConnectorClient();

        public Builder managementUrl(String managementUrl) {
            client.managementUrl = managementUrl;
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
