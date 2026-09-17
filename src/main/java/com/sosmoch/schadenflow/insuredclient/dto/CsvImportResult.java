package com.sosmoch.schadenflow.insuredclient.dto;

import java.util.List;

public record CsvImportResult(
        int totalRows,
        int createdRows,
        int failedRows,
        List<CsvRowError> errors
) {
}
