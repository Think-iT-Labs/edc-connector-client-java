package io.thinkit.edc.client.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.thinkit.edc.client.connector.resource.EdcResource;
import org.junit.jupiter.api.Test;

public class EdcConnectorClientTest {

    @Test
    void should_return_asset_client() {
        var client = EdcConnectorClient.newBuilder()
                .managementUrl("http://127.0.0.1/management/url")
                .build();

        var assets = client.assets();

        assertThat(assets).isNotNull();
    }

    @Test
    void should_thrown_exception_when_requesting_assets_but_no_management_url_specified() {
        var client = EdcConnectorClient.newInstance();

        assertThatThrownBy(client::assets).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_register_extension_resources() {
        var client = EdcConnectorClient.newBuilder()
                .with(ExtensionResource.class, ExtensionResource::new)
                .build();

        var extension = client.resource(ExtensionResource.class);

        assertThat(extension.doStuff()).isEqualTo("done");
    }

    private static class ExtensionResource extends EdcResource {

        protected ExtensionResource(EdcClientContext context) {
            super(context);
        }

        public String doStuff() {
            return "done";
        }
    }
}
