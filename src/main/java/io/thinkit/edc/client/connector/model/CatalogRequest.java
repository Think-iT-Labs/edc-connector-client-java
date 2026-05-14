package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;

public interface CatalogRequest {
    String TYPE_CATALOG_REQUEST = EDC_NAMESPACE + "CatalogRequest";

    String id();

    String protocol();

    String counterPartyAddress();

    String counterPartyId();

    QuerySpec querySpec();
}
