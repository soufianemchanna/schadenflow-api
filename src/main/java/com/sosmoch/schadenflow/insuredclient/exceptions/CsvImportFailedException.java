package com.sosmoch.schadenflow.insuredclient.exceptions;

public class CsvImportFailedException extends RuntimeException{
    public CsvImportFailedException(String message, Exception e) {
        super(message, e);
    }
}
