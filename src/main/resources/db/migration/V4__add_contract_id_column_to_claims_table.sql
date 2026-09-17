ALTER TABLE claims
    ADD contract_id UUID;

ALTER TABLE claims
    ALTER COLUMN contract_id SET NOT NULL;