-- hibernate sequence

CREATE SEQUENCE hibernate_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START WITH 54
    CACHE 1;

-- activity

CREATE TABLE activity
(
    id      bigint NOT NULL,
    action  integer,
    date    timestamp,
    object  character varying(255),
    user_id character varying(255),
    CONSTRAINT activity_pkey PRIMARY KEY (id)
);

-- cycles

CREATE TABLE cycle
(
    id                 bigint                 NOT NULL,
    cycle_state        integer,
    factual_end_date   date,
    factual_start_date date,
    name               character varying(255) NOT NULL,
    planned_end_date   date                   NOT NULL,
    planned_start_date date                   NOT NULL,
    is_visible         bit default 1,
    CONSTRAINT cycle_pkey PRIMARY KEY (id)
);

-- okr units

CREATE TABLE okr_unit
(
    id    bigint                 NOT NULL,
    label character varying(255) NOT NULL,
    name  character varying(255) NOT NULL,
    CONSTRAINT okr_unit_pkey PRIMARY KEY (id)
);

CREATE TABLE okr_child_unit
(
    id bigint,
    parent_okr_unit_id bigint,
    is_active bit,
    CONSTRAINT okr_child_unit_pkey PRIMARY KEY (id),
    CONSTRAINT okr_child_unit_okr_unit_fkey1 FOREIGN KEY (id) REFERENCES okr_unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT okr_child_unit_okr_unit_fkey2 FOREIGN KEY (parent_okr_unit_id) REFERENCES okr_unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE okr_company_history
(
    id bigint NOT NULL,
    CONSTRAINT okr_company_history_pkey PRIMARY KEY (id)
);

CREATE TABLE okr_company
(
    id         bigint NOT NULL,
    cycle_id   bigint,
    history_id bigint,
    CONSTRAINT company_cycle_fkey FOREIGN KEY (cycle_id)
        REFERENCES cycle (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT company_company_history_fkey FOREIGN KEY (history_id)
        REFERENCES okr_company_history (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT okr_unit_fkey FOREIGN KEY (id)
        REFERENCES okr_unit (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE okr_branch
(
    id                      bigint NOT NULL,
    CONSTRAINT okr_branch_pkey PRIMARY KEY (id),
    CONSTRAINT okr_branch_okr_child_unit_fkey FOREIGN KEY (id)
        REFERENCES okr_child_unit (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE okr_department
(
    id                   bigint NOT NULL,
    okr_master_id        uniqueidentifier,
    okr_topic_sponsor_id uniqueidentifier,
    CONSTRAINT department_pkey PRIMARY KEY (id),
    CONSTRAINT department_okr_child_unit_fkey FOREIGN KEY (id)
        REFERENCES okr_child_unit (id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- objectives & key results

CREATE TABLE objective
(
    id                  bigint NOT NULL,
    contact_person_id   character varying(255),
    description         character varying(255),
    name                character varying(255),
    remark              character varying(255),
    review              character varying(255),
    is_active           bit default 1,
    sequence            int     default 0,
    parent_objective_id bigint,
    parent_okr_unit_id bigint,
    CONSTRAINT objective_pkey PRIMARY KEY (id),
    CONSTRAINT objective_okr_unit_fkey FOREIGN KEY (parent_okr_unit_id)
        REFERENCES okr_unit (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT objective_objective_fkey FOREIGN KEY (parent_objective_id)
        REFERENCES objective (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE key_result
(
    id                  bigint NOT NULL,
    current_value       bigint NOT NULL default 0,
    description         character varying(255),
    name                character varying(255),
    start_value         bigint NOT NULL default 0,
    target_value        bigint NOT NULL default 0,
    unit                integer,
    sequence            int             default 0,
    parent_objective_id bigint,
    CONSTRAINT key_result_pkey PRIMARY KEY (id),
    CONSTRAINT key_result_objective_fkey FOREIGN KEY (parent_objective_id)
        REFERENCES objective (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE key_result_milestone
(
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    value bigint NOT NULL,
    parent_key_result_id bigint,
    CONSTRAINT pk_key_result_milestones PRIMARY KEY (id),
    CONSTRAINT fk_key_result_milesones_keyresults FOREIGN KEY (parent_key_result_id) REFERENCES key_result (id)
);

CREATE TABLE note
(
    id                   bigint    NOT NULL,
    text                 character varying(255),
    date                 timestamp NOT NULL default CURRENT_TIMESTAMP,
    user_id              uniqueidentifier,
    parent_key_result_id bigint,
    CONSTRAINT note_pkey PRIMARY KEY (id),
    CONSTRAINT note_key_result_fkey FOREIGN KEY (parent_key_result_id)
        REFERENCES key_result (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE okr_member
(
    okr_department_id bigint NOT NULL,
    okr_member_id uniqueidentifier,
    CONSTRAINT okr_member_department_fkey FOREIGN KEY (okr_department_id)
        REFERENCES okr_department (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- users

CREATE TABLE admin_user
(
    id uniqueidentifier NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE aad_user
(
    id            uniqueidentifier                   NOT NULL,
    given_name    character varying(255) NOT NULL,
    surname       character varying(255) NOT NULL,
    mail          character varying(255) NOT NULL,
    mail_nickname character varying(255) NOT NULL,
    job_title     character varying(255),
    department    character varying(255),
    photo         TEXT                   NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE TABLE local_user
(
    id         uniqueidentifier NOT NULL,
    given_name character varying NOT NULL,
    surname    character varying NOT NULL,
    mail       character varying UNIQUE NOT NULL,
    job_title  character varying,
    department character varying,
    photo      character varying,
    active     bit,
    created_at timestamp default CURRENT_TIMESTAMP NOT NULL,
    password   character varying,
    CONSTRAINT local_user_pkey PRIMARY KEY (id)
);

CREATE TABLE password_token
(
    email_identifier uniqueidentifier                        NOT NULL,
    local_user_id    uniqueidentifier                        NOT NULL,
    created_at       timestamp NOT NULL,
    CONSTRAINT password_token_pkey PRIMARY KEY (email_identifier)
);

-- logging & configurations

CREATE TABLE log
(
    id          BIGINT                 NOT NULL,
    level       CHARACTER VARYING(255) NOT NULL,
    timestamp   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file_name   CHARACTER VARYING(255) NOT NULL,
    line_number CHARACTER VARYING(255) NOT NULL,
    message     TEXT,
    CONSTRAINT log_pkey PRIMARY KEY (id)
);

CREATE TABLE init_state
(
    id bigint NOT NULL,
    init_state integer,
    CONSTRAINT init_state_pkey PRIMARY KEY (id)
);

INSERT INTO init_state (id, init_state)
VALUES (1, 1);

CREATE TABLE oauth_client_details
(
    client_id character varying(255),
    resource_ids character varying(255),
    client_secret character varying(255),
    scope character varying(255),
    authorized_grant_types character varying(255),
    web_server_redirect_uri character varying(255),
    authorities character varying(255),
    access_token_validity integer,
    refresh_token_validity integer,
    additional_information character varying(4096),
    autoapprove character varying(255),
    PRIMARY KEY (client_id)
);

CREATE TABLE oauth_configuration
(
    [key] character varying(255) NOT NULL,
    value character varying(255),
    type character varying(255),
    CONSTRAINT pk_oauth_configuration PRIMARY KEY ([key])
);

INSERT INTO oauth_configuration ([key], value, type)VALUES ('client-id', 'clientapp', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('client-secret', '', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('scope', 'USER', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('redirect-uri', '', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('silent-refresh-redirect-uri', '', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('auth-type', 'local', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('issuer', '', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('response-type', 'password,refresh_token', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('oidc', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('show-debug-information', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('require-https', 'false', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('strict-discovery-document-validation', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('use-http-basic-auth', 'true', 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('access-token-uri', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('user-authorization-uri', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('client-authentication-scheme', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('grant-type', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('auto-approve-scopes', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('token-name', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('user-info-uri', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('prefer-token-info', null, 'text');
INSERT INTO oauth_configuration ([key], value, type)VALUES ('token-endpoint', '/oauth/token', 'text');

CREATE TABLE user_settings
(
    id                 bigint NOT NULL,
    user_id            uniqueidentifier   NOT NULL,
    default_okr_company_id bigint,
    default_team_id    bigint,
    CONSTRAINT user_settings_pkey PRIMARY KEY (id),
    CONSTRAINT user_settings_company_fkey FOREIGN KEY (default_okr_company_id)
        REFERENCES okr_company (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT user_settings_team_fkey FOREIGN KEY (default_team_id)
        REFERENCES okr_department (id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE configuration
(
    id    bigint                 NOT NULL,
    name  character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    type character varying(255),
    PRIMARY KEY (id)
);

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'max-key-results', '7', 'number');

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'objective-progress-green-yellow-threshold', '-0.2', 'number');

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'objective-progress-yellow-red-threshold', '-0.4', 'number');

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'general_frontend-base-url', 'http://localhost:4200', 'text');

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'email_from', 'no-reply@burning-okr.com', 'text');

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'email_subject_new-user', 'Please set your password for BurningOKR', 'text');

INSERT INTO configuration(id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'email_subject_forgot-password', 'Reset your Password for BurningOKR', 'text');

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'email_subject_feedback', 'OKR Tool - Feedback', 'text');

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'feedback_receivers', 'feedback@burning-okr.com', 'text');

INSERT INTO configuration (id, name, value, type)
VALUES (NEXT VALUE FOR hibernate_sequence, 'topic-sponsors-activated', 'true', 'checkbox');
