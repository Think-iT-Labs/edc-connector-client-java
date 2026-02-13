package io.thinkit.edc.client.connector.model;

import java.util.List;

public interface ContractNegotiation {
    String id();

    String type();

    String protocol();

    String counterPartyId();

    String counterPartyAddress();

    String state();

    String contractAgreementId();

    String errorDetail();

    List<? extends CallbackAddress> callbackAddresses();

    long createdAt();
}
