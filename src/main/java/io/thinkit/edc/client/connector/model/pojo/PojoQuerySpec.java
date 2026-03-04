package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.thinkit.edc.client.connector.model.QuerySpec;
import java.util.List;

public class PojoQuerySpec implements QuerySpec {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("@id")
    private String id;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("sortOrder")
    private String sortOrder;

    @JsonProperty("sortField")
    private String sortField;

    @JsonProperty("filterExpression")
    private List<PojoCriterion> filterExpression;

    @JsonProperty("createdAt")
    private long createdAt;

    @Override
    public String id() {
        return id;
    }

    @Override
    public int offset() {
        return offset;
    }

    @Override
    public int limit() {
        return limit;
    }

    @Override
    public String sortOrder() {
        return sortOrder;
    }

    @Override
    public String sortField() {
        return sortField;
    }

    @Override
    public List<PojoCriterion> filterExpression() {
        return filterExpression != null ? filterExpression : List.of();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final PojoQuerySpec request = new PojoQuerySpec();

        public static PojoQuerySpec.Builder newInstance() {
            return new PojoQuerySpec.Builder();
        }

        public PojoQuerySpec.Builder offset(int offset) {
            request.offset = offset;
            return this;
        }

        public PojoQuerySpec.Builder limit(int limit) {
            request.limit = limit;
            return this;
        }

        public PojoQuerySpec.Builder sortOrder(String sortOrder) {
            request.sortOrder = sortOrder;
            return this;
        }

        public PojoQuerySpec.Builder sortField(String sortField) {
            request.sortField = sortField;
            return this;
        }

        public PojoQuerySpec.Builder filterExpression(List<PojoCriterion> filterExpression) {
            request.filterExpression = filterExpression;
            return this;
        }

        public PojoQuerySpec build() {
            request.type = TYPE_QUERY_SPEC;
            return request;
        }
    }
}
