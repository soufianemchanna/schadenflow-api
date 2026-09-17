package com.sosmoch.schadenflow.insuredclient.internal.csv;


import com.sosmoch.schadenflow.insuredclient.dto.CsvImportResult;
import com.sosmoch.schadenflow.insuredclient.dto.CsvRowError;
import com.sosmoch.schadenflow.insuredclient.exceptions.CsvImportFailedException;
import com.sosmoch.schadenflow.insuredclient.exceptions.CsvImportMissingHeader;
import com.sosmoch.schadenflow.insuredclient.exceptions.CsvRowException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CsvImportRunner {
    private static final int BATCH_SIZE = 100;

    public <T>CsvImportResult run(byte[] fileContent, CsvImportStrategy<T> strategy){
        List<T> batch = new ArrayList<>();
        List<CsvRowError> errors = new ArrayList<>();
        int successCount = 0;
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true).get();

        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(fileContent));

             CSVParser parser = format.parse(reader)) {

            // check required headers match csv file headers
            for(String required : strategy.requiredHeaders()){
                if(!parser.getHeaderMap().containsKey(required)){
                    throw new CsvImportMissingHeader("Missing required header " + required);
                }
            }

            for (CSVRecord record : parser) {
                try {
                    T entity = strategy.mapRow(record);
                    batch.add(entity);
                    successCount++;

                    if (batch.size() == BATCH_SIZE) {
                        strategy.saveBatch(batch);
                        batch.clear();
                    }
                } catch (CsvRowException e) {
                    errors.add(new CsvRowError(record.getRecordNumber(), e.getMessage()));
                }
            }
            if (!batch.isEmpty()) {
                strategy.saveBatch(batch);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CsvImportFailedException("Unreadable file or missing headers", e);
        }

        return new CsvImportResult(successCount+errors.size(),successCount,errors.size() ,errors);

    }
}
