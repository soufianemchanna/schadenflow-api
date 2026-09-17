ALTER TABLE async_jobs
    DROP COLUMN errors_json;

ALTER TABLE async_jobs
    ADD errors_json TEXT;