package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;

import java.util.List;

public interface QuerySpec {

    String TYPE_QUERY_SPEC = EDC_NAMESPACE + "QuerySpec";

    String id();

    int offset();

    int limit();

    String sortOrder();

    public String sortField();

    List<? extends Criterion> filterExpression();
}
