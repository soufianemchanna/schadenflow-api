package com.sosmoch.schadenflow.insuredclient.exceptions;

public class InsuredClientNotFoundException extends RuntimeException {
    public InsuredClientNotFoundException(String identifier) {
        super("Insured client with identifier " + identifier + " not found.");
    }
}
