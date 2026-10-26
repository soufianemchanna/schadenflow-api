package com.sosmoch.schadenflow.agent.exceptions;

public class AgentNotFoundException extends RuntimeException {
    public AgentNotFoundException(String identifier) {
        super("Agent with identifier " + identifier + " not found.");
    }
}
