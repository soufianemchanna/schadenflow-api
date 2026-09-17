package com.sosmoch.schadenflow.insuredclient.internal.csv;

import org.apache.commons.csv.CSVRecord;

import java.util.List;

public interface CsvImportStrategy<T> {
    String[] requiredHeaders();
    T mapRow(CSVRecord record);
    void saveBatch(List<T> batch);
}
