package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.*;

public record CatalogRequest(String protocol, String counterPartyAddress, QuerySpec querySpec) {}
