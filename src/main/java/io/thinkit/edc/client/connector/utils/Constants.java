package io.thinkit.edc.client.connector.utils;

import java.util.List;

public interface Constants {
    String EDC_NAMESPACE = "https://w3id.org/edc/v0.0.1/ns/";
    String MANAGEMENT_V2_CONTEXT = "https://w3id.org/edc/connector/management/v2";
    // @context must be serialized as a JSON array. The JSON-LD compaction algorithm only produces
    // an array when there are at least two entries, so the context URL is intentionally listed twice.
    List<String> MANAGEMENT_V2_CONTEXT_ARRAY = List.of(MANAGEMENT_V2_CONTEXT, MANAGEMENT_V2_CONTEXT);
    String DCAT_NAMESPACE = "http://www.w3.org/ns/dcat#";
    String DCT_NAMESPACE = "http://purl.org/dc/terms/";
    String ODRL_NAMESPACE = "http://www.w3.org/ns/odrl/2/";
    String DSPACE_NAMESPACE = "https://w3id.org/dspace/2025/1/";

    String ID = "@id";
    String TYPE = "@type";
    String VALUE = "@value";
    String CONTEXT = "@context";
    String VOCAB = "@vocab";
    String DCAT_PREFIX = "dcat";
    String DCT_PREFIX = "dct";
    String ODRL_PREFIX = "odrl";
    String DSCPACE_PREFIX = "dspace";
}
