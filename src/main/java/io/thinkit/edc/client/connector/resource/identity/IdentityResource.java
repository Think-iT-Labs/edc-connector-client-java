package io.thinkit.edc.client.connector.resource.identity;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.resource.EdcResource;

public class IdentityResource extends EdcResource {

    protected final String identityUrl;

    protected IdentityResource(EdcClientContext context) {
        super(context);
        identityUrl = context.urls().identityUrl();
        if (identityUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate %s client without the identity url"
                    .formatted(getClass().getSimpleName()));
        }
    }
}
