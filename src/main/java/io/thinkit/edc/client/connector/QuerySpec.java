package io.thinkit.edc.client.connector;

import static io.thinkit.edc.client.connector.Constants.CONTEXT;
import static io.thinkit.edc.client.connector.Constants.EDC_NAMESPACE;
import static io.thinkit.edc.client.connector.Constants.TYPE;
import static io.thinkit.edc.client.connector.Constants.VALUE;
import static io.thinkit.edc.client.connector.Constants.VOCAB;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;
import static jakarta.json.stream.JsonCollectors.toJsonArray;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
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

    public static final class Builder {

        private final JsonObjectBuilder raw = createObjectBuilder()
                .add(CONTEXT, createObjectBuilder().add(VOCAB, EDC_NAMESPACE))
                .add(TYPE, TYPE_QUERY_SPEC);

        public static Builder newInstance() {
            return new Builder();
        }

        public QuerySpec build() {
            return new QuerySpec(raw.build());
        }

        public Builder offset(int offset) {
            raw.add(
                    QUERY_SPEC_OFFSET,
                    createArrayBuilder().add(createObjectBuilder().add(VALUE, offset)));
            return this;
        }

        public Builder limit(int limit) {
            raw.add("limit", createArrayBuilder().add(createObjectBuilder().add(VALUE, limit)));
            return this;
        }

        public Builder sortOrder(String sortOrder) {
            raw.add("sortOrder", createArrayBuilder().add(createObjectBuilder().add(VALUE, sortOrder)));
            return this;
        }

        public Builder sortField(String sortField) {
            raw.add("sortField", createArrayBuilder().add(createObjectBuilder().add(VALUE, sortField)));
            return this;
        }

        public Builder filterExpression(List<Criterion> criteria) {
            raw.add("filterExpression", criteria.stream().map(Criterion::raw).collect(toJsonArray()));
            return this;
        }
    }
}
