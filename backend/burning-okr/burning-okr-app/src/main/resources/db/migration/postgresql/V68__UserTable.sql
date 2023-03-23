CREATE TABLE "user"
(
    id         UUID    NOT NULL,
    given_name VARCHAR(255),
    surname    VARCHAR(255),
    mail       VARCHAR(255),
    job_title  VARCHAR(255),
    department VARCHAR(255),
    photo      VARCHAR(255),
    active     BOOLEAN NOT NULL,
    admin      BOOLEAN NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user PRIMARY KEY (id)
);
