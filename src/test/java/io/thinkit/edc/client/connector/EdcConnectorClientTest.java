package io.thinkit.edc.client.connector;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EdcConnectorClientTest {

    @Test
    void should_return_asset_client() {
        var client = EdcConnectorClient.newInstance()
                .managementUrl("http://127.0.0.1/management/url");

        var assets = client.assets();

        assertThat(assets).isNotNull();
    }

    @Test
    void should_thrown_exception_when_requesting_assets_but_no_management_url_specified() {
        var client = EdcConnectorClient.newInstance();

        assertThatThrownBy(client::assets).isInstanceOf(IllegalArgumentException.class);
    }
}
