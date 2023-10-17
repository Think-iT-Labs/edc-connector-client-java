package io.thinkit.edc.client.connector;

import java.util.List;

public record ContractDefinitionInput(
        String id, String accessPolicyId, String contractPolicyId, List<CriterionInput> assetsSelector) {}
