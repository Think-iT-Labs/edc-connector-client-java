package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.utils.Constants.TYPE;
import static io.thinkit.edc.client.connector.utils.Constants.VALUE;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.thinkit.edc.client.connector.utils.JsonLdObject;
import jakarta.json.JsonObject;
import java.util.List;

public class QuerySpec extends JsonLdObject {

    private static final String TYPE_QUERY_SPEC = EDC_NAMESPACE + "QuerySpec";
    private static final String QUERY_SPEC_OFFSET = EDC_NAMESPACE + "offset";
    private static final String QUERY_SPEC_LIMIT = EDC_NAMESPACE + "limit";
    private static final String QUERY_SPEC_SORT_ORDER = EDC_NAMESPACE + "sortOrder";
    private static final String QUERY_SPEC_SORT_FIELD = EDC_NAMESPACE + "sortField";
    private static final String QUERY_SPEC_FILTER_EXPRESSION = EDC_NAMESPACE + "filterExpression";

    private QuerySpec(JsonObject raw) {
        super(raw);
    }

    public int offset() {
        return intValue(QUERY_SPEC_OFFSET);
    }

    public int limit() {
        return intValue(QUERY_SPEC_LIMIT);
    }

    public String sortOrder() {
        return stringValue(QUERY_SPEC_SORT_ORDER);
    }

    public String sortField() {
        return stringValue(QUERY_SPEC_SORT_FIELD);
    }

    public List<Criterion> filterExpression() {
        return objects(QUERY_SPEC_FILTER_EXPRESSION)
                .map(it -> Criterion.Builder.newInstance().raw(it).build())
                .toList();
    }

    public static final class Builder extends AbstractBuilder<QuerySpec, QuerySpec.Builder> {
        public static Builder newInstance() {
            return new Builder();
        }

        public QuerySpec build() {
            return new QuerySpec(builder.add(TYPE, TYPE_QUERY_SPEC).build());
        }

        public Builder offset(int offset) {
            builder.add(
                    QUERY_SPEC_OFFSET,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, offset)));
            return this;
        }

        public Builder limit(int limit) {
            builder.add("limit", createArrayBuilder().add(createObjectBuilder().add(VALUE, limit)));
            return this;
        }

        public Builder sortOrder(String sortOrder) {
            builder.add(
                    "sortOrder", createArrayBuilder().add(createObjectBuilder().add(VALUE, sortOrder)));
            return this;
        }

        public Builder sortField(String sortField) {
            builder.add(
                    "sortField", createArrayBuilder().add(createObjectBuilder().add(VALUE, sortField)));
            return this;
        }

        public Builder filterExpression(List<Criterion> criteria) {
            builder.add(
                    "filterExpression", criteria.stream().map(Criterion::raw).collect(toJsonArray()));
            return this;
        }
    }
}
