package io.thinkit.edc.client.connector.model;

import java.util.List;

public interface ContractDefinition {
    String id();

    String accessPolicyId();

    String contractPolicyId();

    List<? extends Criterion> assetsSelector();

    long createdAt();
}
