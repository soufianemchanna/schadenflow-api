package com.sosmoch.schadenflow.insuredclient.exceptions;

public class CsvImportMissingHeader extends RuntimeException {
    public CsvImportMissingHeader(String message) {
        super(message);
    }
}
