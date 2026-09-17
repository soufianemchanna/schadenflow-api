package com.sosmoch.schadenflow.insuredclient.internal.csv;

import com.sosmoch.schadenflow.insuredclient.enums.ContractStatus;
import com.sosmoch.schadenflow.insuredclient.enums.ProductType;
import com.sosmoch.schadenflow.insuredclient.exceptions.CsvRowException;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClient;
import com.sosmoch.schadenflow.insuredclient.internal.client.InsuredClientRepository;
import com.sosmoch.schadenflow.insuredclient.internal.contract.Contract;
import com.sosmoch.schadenflow.insuredclient.internal.contract.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractCsvImportStrategy implements CsvImportStrategy<Contract> {
    private final InsuredClientRepository insuredClientRepository;
    private final ContractRepository contractRepository;
    @Override
    public String[] requiredHeaders() {
        return new String[]{"clientNumber", "contractNumber", "productType", "status", "startDate", "endDate"};
    }

    @Override
    public Contract mapRow(CSVRecord record) {
        String clientNumber = record.get("clientNumber");
        InsuredClient client = insuredClientRepository.findInsuredClientByClientNumber(clientNumber)
                .orElseThrow(
                        () ->   new CsvRowException("Client with clientNumber " + clientNumber + " not found")
                );

        ProductType productType;
        try{
            productType = ProductType.valueOf(record.get("productType").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CsvRowException("Invalid productType " + record.get("productType"));
        }
        log.info(record.get("status").isEmpty() ? "yes is empty" : "nooo");
        return Contract.builder()
                .contractNumber(record.get("contractNumber"))
                .insuredClient(client)
                .status(
                    record.get("status").isEmpty() ?
                            ContractStatus.ACTIVE
                            : ContractStatus.valueOf(record.get("status").toUpperCase())
                )
                .startDate(LocalDate.parse(record.get("startDate")))
                .endDate(
                        record.get("endDate").isEmpty() ?
                                null
                                : LocalDate.parse(record.get("endDate"))
                )
                .productType(productType)
                .build();
    }

    @Override
    public void saveBatch(List<Contract> batch) {
        contractRepository.saveAll(batch);
    }
}
