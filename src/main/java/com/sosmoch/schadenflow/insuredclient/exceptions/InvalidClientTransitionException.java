package com.sosmoch.schadenflow.insuredclient.exceptions;

import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;

import java.util.UUID;

public class InvalidClientTransitionException extends RuntimeException {
    public InvalidClientTransitionException(UUID id, InsuredClientStatus status, InsuredClientStatus nextStatus) {

    }
}
