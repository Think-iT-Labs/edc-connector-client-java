package io.thinkit.edc.client.connector.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.thinkit.edc.client.connector.model.Policy;
import jakarta.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(builder = PojoPolicy.Builder.class)
public class PojoPolicy implements Policy {

    @JsonProperty("@id")
    private String id;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("target")
    private String target;

    @JsonProperty("assigner")
    private String assigner;

    @JsonProperty("permissions")
    private List<JsonObject> permissions = new ArrayList<>();

    @JsonProperty("prohibitions")
    private List<JsonObject> prohibitions = new ArrayList<>();

    @JsonProperty("obligations")
    private List<JsonObject> obligations = new ArrayList<>();

    private PojoPolicy() {}

    public String id() {
        return id;
    }

    @Override
    public String target() {
        return target != null ? target : "";
    }

    @Override
    public String assigner() {
        return assigner != null ? assigner : "";
    }

    public List<JsonObject> permissions() {
        return permissions;
    }

    public List<JsonObject> prohibitions() {
        return prohibitions;
    }

    public List<JsonObject> obligations() {
        return obligations;
    }

    public static class Builder {
        private final PojoPolicy policy = new PojoPolicy();

        public static Builder newInstance() {
            return new Builder();
        }

        @JsonProperty("@id")
        public Builder id(String id) {
            policy.id = id;
            return this;
        }

        public Builder target(String target) {
            policy.target = target;
            return this;
        }

        public Builder assigner(String assigner) {
            policy.assigner = assigner;
            return this;
        }

        public Builder permissions(List<JsonObject> permissions) {
            policy.permissions = permissions;
            return this;
        }

        public Builder prohibitions(List<JsonObject> prohibitions) {
            policy.prohibitions = prohibitions;
            return this;
        }

        public Builder obligations(List<JsonObject> obligations) {
            policy.obligations = obligations;
            return this;
        }

        public PojoPolicy build() {

            policy.type = TYPE_POLICY;
            return policy;
        }
    }
}
