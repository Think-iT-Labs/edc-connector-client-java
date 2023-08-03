package io.thinkit.edc.client.connector;

public class EdcConnectorClient {
    private String managementUrl;

    public static EdcConnectorClient newInstance() {
        return new EdcConnectorClient();
    }

    private EdcConnectorClient() {
    }

    public EdcConnectorClient managementUrl(String managementUrl) {
        this.managementUrl = managementUrl;
        return this;
    }

    public Assets assets() {
        if (managementUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate Assets client without the management url");
        }
        return new Assets(managementUrl);
    }

}
