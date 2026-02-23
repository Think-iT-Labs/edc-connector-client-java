package io.thinkit.edc.client.connector.model;

import static io.thinkit.edc.client.connector.utils.Constants.EDC_NAMESPACE;

import java.util.List;

public interface ContractRequest {
    String TYPE_CONTRACT_REQUEST = EDC_NAMESPACE + "ContractRequest";

    String counterPartyAddress();

    String protocol();

    Policy policy();

    List<? extends CallbackAddress> callbackAddresses();
}
