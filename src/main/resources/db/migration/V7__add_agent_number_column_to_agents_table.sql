CREATE SEQUENCE agent_number_seq START WITH 1 INCREMENT BY 1;

ALTER TABLE agents
    ADD COLUMN agent_number VARCHAR(12) NOT NULL
        DEFAULT 'AGT-' || LPAD(nextval('agent_number_seq')::text, 6, '0');

ALTER TABLE agents
    ADD CONSTRAINT uq_agents_agent_number UNIQUE (agent_number);