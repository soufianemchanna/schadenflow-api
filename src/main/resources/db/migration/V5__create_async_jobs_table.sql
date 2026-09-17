CREATE TABLE async_jobs
(
    id            UUID    NOT NULL,
    status        VARCHAR(255),
    success_count INTEGER NOT NULL,
    total_count   INTEGER NOT NULL,
    errors_json   OID,
    started_at    TIMESTAMP WITHOUT TIME ZONE,
    finished_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_async_jobs PRIMARY KEY (id)
);