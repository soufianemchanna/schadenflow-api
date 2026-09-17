ALTER TABLE insured_clients
DROP
COLUMN start_date;

CREATE TABLE contracts
(
    id                UUID         NOT NULL,
    deleted_at        TIMESTAMP WITHOUT TIME ZONE,
    contract_number   VARCHAR(255) NOT NULL,
    insured_client_id UUID         NOT NULL,
    product_type      VARCHAR(255) NOT NULL,
    status            VARCHAR(255) NOT NULL,
    start_date        date         NOT NULL,
    end_date          date,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_contracts PRIMARY KEY (id)
);

ALTER TABLE contracts
    ADD CONSTRAINT uk_contracts_contract_number UNIQUE (contract_number);

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_insured_client FOREIGN KEY (insured_client_id) REFERENCES insured_clients (id);