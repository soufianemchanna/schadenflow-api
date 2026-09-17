
-- required extensions
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- users definition

-- Drop table

-- DROP TABLE users;

CREATE TABLE users (
      id uuid DEFAULT gen_random_uuid() NOT NULL,
      email varchar(255) NOT NULL,
      last_login TIMESTAMPTZ NULL,
      password_hash varchar(255) NOT NULL,
      user_role varchar(255) NOT NULL,
      created_at TIMESTAMPTZ NOT NULL,
      deleted_at TIMESTAMPTZ NULL,
      CONSTRAINT uk_users_email UNIQUE (email),
      CONSTRAINT pk_users PRIMARY KEY (id),
      CONSTRAINT ck_users_role
          CHECK (user_role IN ('ADMIN', 'AGENT', 'CLIENT'))
);
-- agents definition

-- Drop table

-- DROP TABLE agents;

CREATE TABLE agents (
        id                  uuid         DEFAULT gen_random_uuid() NOT NULL,
        active_claims_count int4         NOT NULL,
        available           bool         NOT NULL,
        email               varchar(255) NOT NULL,
        agent_name          varchar(255) NOT NULL,
        specialization      varchar(255) NOT NULL,
        user_id             uuid NULL,
        created_at          TIMESTAMPTZ NOT NULL,
        deleted_at          TIMESTAMPTZ NULL,
        CONSTRAINT pk_agents PRIMARY KEY (id),
        CONSTRAINT ck_agents_specialization
            CHECK (specialization IN ('COMMERCIAL_AND_LIABILITY', 'HEALTH_AND_ACCIDENT', 'MOTOR_VEHICLE', 'HEALTH_AND_ACCIDENT', 'PROPERTY_AND_HOUSEHOLD', 'LIFE_AND_PENSIONS', 'TRAVEL_AND_LEISURE', 'ALL')),
        CONSTRAINT uk_agents_email UNIQUE (email),
        CONSTRAINT fk_agent_user FOREIGN KEY (user_id) REFERENCES users(id)
);


-- audit_logs definition

-- Drop table

-- DROP TABLE audit_logs;

CREATE TABLE audit_logs (
       id uuid DEFAULT gen_random_uuid() NOT NULL,
       performed_action varchar(255) NOT NULL,
       details varchar(255) NULL,
       entity_id uuid NOT NULL,
       entity_type varchar(255) NOT NULL,
       new_value varchar(500) NULL,
       old_value varchar(500) NULL,
       performed_by varchar(255) NOT NULL,
       performed_at TIMESTAMPTZ NOT NULL,
       deleted_at TIMESTAMPTZ NULL,
       CONSTRAINT pk_audit_logs PRIMARY KEY (id)
);

-- insured_clients definition

-- Drop table

-- DROP TABLE insured_clients;

CREATE TABLE insured_clients (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    client_number varchar(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    email varchar(255) NOT NULL,
    full_name varchar(255) NOT NULL,
    gender varchar(255) NOT NULL,
    product_type varchar(255) NOT NULL,
    start_date date NOT NULL,
    status varchar(255) NOT NULL,
    user_id uuid NULL,
    registered_at TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ NULL,
    CONSTRAINT ck_insured_clients_gender CHECK (gender in ('MALE', 'FEMALE')),
	CONSTRAINT pk_insured_clients PRIMARY KEY (id),
	CONSTRAINT ck_insured_clients_product_type
	    CHECK (product_type IN ('COMMERCIAL_AND_LIABILITY', 'HEALTH_AND_ACCIDENT', 'MOTOR_VEHICLE', 'HEALTH_AND_ACCIDENT', 'PROPERTY_AND_HOUSEHOLD', 'LIFE_AND_PENSIONS', 'TRAVEL_AND_LEISURE')),
	CONSTRAINT ck_insured_clients_status
	    CHECK (status in ('PENDING_REGISTRATION', 'ACTIVE', 'SUSPENDED')),
	CONSTRAINT uk_insured_clients_email UNIQUE (email),
	CONSTRAINT uk_insured_clients_client_number UNIQUE (client_number),
    CONSTRAINT fk_client_user FOREIGN KEY (user_id) REFERENCES users(id)

);


-- claims definition

-- Drop table

-- DROP TABLE claims;

CREATE TABLE claims (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    amount numeric(12, 2) NULL,
    assigned_agent_id uuid NULL,
    claim_type varchar(255) NOT NULL,
    description varchar(2000) NOT NULL,
    insured_client_id uuid NOT NULL,
    rejection_reason varchar(255) NULL,
    status varchar(255) NOT NULL,
    submitted_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ NULL,
    CONSTRAINT claims_claim_type_check
        CHECK (claim_type IN ('COMMERCIAL_AND_LIABILITY', 'HEALTH_AND_ACCIDENT', 'MOTOR_VEHICLE', 'HEALTH_AND_ACCIDENT', 'PROPERTY_AND_HOUSEHOLD', 'LIFE_AND_PENSIONS', 'TRAVEL_AND_LEISURE')),
	CONSTRAINT pk_claims PRIMARY KEY (id),
	CONSTRAINT fk_claim_agent FOREIGN KEY (assigned_agent_id) REFERENCES agents(id),
	CONSTRAINT fk_claim_client FOREIGN KEY (insured_client_id) REFERENCES insured_clients(id),
	CONSTRAINT claims_status_check CHECK (status in ('SUBMITTED', 'UNDER_REVIEW', 'VALIDATED', 'REJECTED', 'ASSIGNED', 'IN_PROGRESS', 'NEED_MORE_INFO', 'RESOLVED', 'CLOSED'))
);




-- notifications definition

-- Drop table

-- DROP TABLE notifications;

CREATE TABLE notifications (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    claim_id uuid NOT NULL,
    message varchar(255) NOT NULL,
    is_read bool DEFAULT false,
    recipient_id uuid NOT NULL,
    notif_type varchar(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id),
    CONSTRAINT fk_notification_user FOREIGN KEY (recipient_id) REFERENCES users(id),
    CONSTRAINT fk_notification_claim FOREIGN KEY (claim_id) REFERENCES claims(id)

);




-- agent_notes definition

-- Drop table

-- DROP TABLE agent_notes;

CREATE TABLE agent_notes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    agent_id uuid NOT NULL,
    note_content varchar(2000) NOT NULL,
    claim_id uuid NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ NULL,
    CONSTRAINT pk_agent_notes PRIMARY KEY (id),
    CONSTRAINT fk_agent_note_claim FOREIGN KEY (claim_id) REFERENCES claims(id),
    CONSTRAINT fk_agent_note_agent FOREIGN KEY (agent_id) REFERENCES agents(id)
);


-- claim_documents definition

-- Drop table

-- DROP TABLE claim_documents;

CREATE TABLE claim_documents (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    bucket_name varchar(255) NOT NULL,
    file_name varchar(255) NOT NULL,
    file_size_bytes int8 NULL,
    mime_type varchar(255) NOT NULL,
    object_key varchar(255) NOT NULL,
    stored_file_name varchar(255) NOT NULL,
    claim_id uuid NOT NULL,
    uploaded_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ NULL,
    CONSTRAINT pk_claim_documents PRIMARY KEY (id),
    CONSTRAINT fk_claim_document_claim FOREIGN KEY (claim_id) REFERENCES claims(id)
);


-- INDEXES (FKs)

CREATE INDEX idx_claims_agent ON claims(assigned_agent_id);
CREATE INDEX idx_claims_client ON claims(insured_client_id);

CREATE INDEX idx_notifications_claim ON notifications(claim_id);
CREATE INDEX idx_notifications_user ON notifications(recipient_id);

CREATE INDEX idx_agent_notes_claim ON agent_notes(claim_id);
CREATE INDEX idx_agent_notes_agent ON agent_notes(agent_id);

CREATE INDEX idx_claim_documents_claim ON claim_documents(claim_id);