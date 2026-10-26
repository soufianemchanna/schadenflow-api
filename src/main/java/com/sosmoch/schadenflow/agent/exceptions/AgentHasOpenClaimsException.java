package com.sosmoch.schadenflow.agent.exceptions;

public class AgentHasOpenClaimsException extends RuntimeException {
    public AgentHasOpenClaimsException() {
        super("Agent has open claims.");
    }
}
