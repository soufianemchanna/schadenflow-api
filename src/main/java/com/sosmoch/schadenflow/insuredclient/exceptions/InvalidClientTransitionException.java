package com.sosmoch.schadenflow.insuredclient.exceptions;

import com.sosmoch.schadenflow.insuredclient.enums.InsuredClientStatus;

import java.util.UUID;

public class InvalidClientTransitionException extends RuntimeException {
    public InvalidClientTransitionException(UUID id, InsuredClientStatus status, InsuredClientStatus nextStatus) {
        super("Insured client with id " + id + " cannot be transitioned from " + status + " to " + nextStatus);

    }
}
