
ALTER TABLE agents DROP CONSTRAINT IF EXISTS uk_agents_email;
CREATE UNIQUE INDEX uq_agents_email_live
    ON agents (email) WHERE deleted_at IS NULL;

ALTER TABLE users DROP CONSTRAINT IF EXISTS uk_users_email;
CREATE UNIQUE INDEX uq_users_email_live
    ON users (email) WHERE deleted_at IS NULL;


ALTER TABLE insured_clients DROP CONSTRAINT IF EXISTS uk_insured_clients_email;
CREATE UNIQUE INDEX uk_insured_clients_email_live
    ON insured_clients (email) WHERE deleted_at IS NULL;