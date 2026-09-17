package com.sosmoch.schadenflow.insuredclient.exceptions;

import java.util.UUID;

public class ContractOwnershipMismatchException extends RuntimeException {
    public ContractOwnershipMismatchException(UUID contractId, UUID insuredClientId) {
    }
}
