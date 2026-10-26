package com.sosmoch.schadenflow.insuredclient.exceptions;

import java.util.UUID;

public class InactiveContractException extends RuntimeException {
    public InactiveContractException(String identifier) {
        super("Contract with identifier " + identifier + " is inactive.");
    }
}
