package com.sosmoch.schadenflow.insuredclient.exceptions;

import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;

import java.util.UUID;

public class InvalidContractTransitionException extends RuntimeException {
    public InvalidContractTransitionException(String identifier, ContractStatus status, ContractStatus nextStatus) {
    }
}
