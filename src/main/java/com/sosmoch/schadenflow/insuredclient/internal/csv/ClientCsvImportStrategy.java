package com.sosmoch.schadenflow.insuredclient.internal.csv;


import com.sosmoch.schadenflow.insuredclient.enums.Gender;
import com.sosmoch.schadenflow.insuredclient.exceptions.CsvRowException;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClient;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClientRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor

public class ClientCsvImportStrategy implements CsvImportStrategy<InsuredClient> {

    private final InsuredClientRepository insuredClientRepository;

    @Override
    public String[] requiredHeaders() {
        return new String[]{"clientNumber", "email", "dateOfBirth", "fullName", "gender"};
    }

    @Override
    public InsuredClient mapRow(CSVRecord record) {
        String clientNumber = record.get("clientNumber");
        if (insuredClientRepository.existsByClientNumber(clientNumber)){
            throw new CsvRowException("Client with clientNumber " + clientNumber + " already exists");
        }
        return InsuredClient.builder()
                .clientNumber(clientNumber)
                .email(record.get("email"))
                .dateOfBirth(LocalDate.parse(record.get("dateOfBirth")))
                .fullName(record.get("fullName"))
                .gender(Gender.valueOf(record.get("gender").toUpperCase()))
                .build();
    }

    @Override
    public void saveBatch(List<InsuredClient> batch) {
        insuredClientRepository.saveAll(batch);
    }
}
