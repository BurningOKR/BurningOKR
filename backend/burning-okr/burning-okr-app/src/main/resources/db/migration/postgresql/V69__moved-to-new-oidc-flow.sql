CREATE TABLE "user"
(
    id         UUID    NOT NULL,
    given_name VARCHAR,
    surname    VARCHAR,
    mail       VARCHAR,
    job_title  VARCHAR,
    department VARCHAR,
    photo      VARCHAR,
    active     BOOLEAN NOT NULL,
    admin      BOOLEAN NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

-- Drop unused tables
DROP TABLE admin_user CASCADE;
DROP TABLE auditor_user CASCADE;
DROP TABLE password_token CASCADE;

-- migrate aad_user-table
INSERT INTO "user" (id, given_name, surname, mail, job_title, department, photo, active, admin, created_at)
    SELECT id, given_name, surname, mail, job_title, department, photo, true, false, LOCALTIMESTAMP FROM aad_user;

-- migrate local_user-table and add deprecated to name for indicating, that local user can't be used anymore and need to
-- be manually migrated
INSERT INTO "user" (id, given_name, surname, mail, job_title, department, photo, active, admin, created_at)
    SELECT id, given_name, surname || ' DEPRECATED', mail, job_title, department, photo, active, false, created_at FROM local_user;

-- Drop old user-tables
DROP TABLE aad_user CASCADE;
DROP TABLE local_user CASCADE;