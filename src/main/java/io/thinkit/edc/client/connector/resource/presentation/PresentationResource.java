package io.thinkit.edc.client.connector.resource.presentation;

import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.resource.EdcResource;

public class PresentationResource extends EdcResource {

    protected final String presentationUrl;

    protected PresentationResource(EdcClientContext context) {
        super(context);
        presentationUrl = context.urls().presentation();
        if (presentationUrl == null) {
            throw new IllegalArgumentException("Cannot instantiate %s client without the presenttaion url"
                    .formatted(getClass().getSimpleName()));
        }
    }
}
