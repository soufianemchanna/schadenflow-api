package com.sosmoch.schadenflow.core.job;

public class AsyncJobNotFoundException extends RuntimeException {
    public AsyncJobNotFoundException(String message) {
        super(message);
    }
}
