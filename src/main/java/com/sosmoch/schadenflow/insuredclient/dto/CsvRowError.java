package com.sosmoch.schadenflow.insuredclient.dto;

public record CsvRowError(
        long rowNumber,
        String reason
) {
}
