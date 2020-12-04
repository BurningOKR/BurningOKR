-- hibernate sequence

CREATE SEQUENCE public.hibernate_sequence
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 54
    CACHE 1;

-- activity

CREATE TABLE public.activity
(
    id      bigint NOT NULL,
    action  integer,
    date    timestamp without time zone,
    object  character varying(255),
    user_id character varying(255),
    CONSTRAINT activity_pkey PRIMARY KEY (id)
) WITH (
      OIDS= FALSE
    );

-- cycles

CREATE TABLE public.cycle
(
    id                 bigint                 NOT NULL,
    cycle_state        integer,
    factual_end_date   date,
    factual_start_date date,
    name               character varying(255) NOT NULL,
    planned_end_date   date                   NOT NULL,
    planned_start_date date                   NOT NULL,
    is_visible         boolean default true,
    CONSTRAINT cycle_pkey PRIMARY KEY (id)
) WITH (
      OIDS= FALSE
    );

-- okr units

CREATE TABLE public.okr_unit
(
    id    bigint                 NOT NULL,
    label character varying(255) NOT NULL,
    name  character varying(255) NOT NULL,
    CONSTRAINT okr_unit_pkey PRIMARY KEY (id)
) WITH (
      OIDS= FALSE
    );

CREATE TABLE public.okr_child_unit
(
    id bigint,
    parent_okr_unit_id bigint,
    is_active boolean,
    CONSTRAINT okr_child_unit_pkey PRIMARY KEY (id),
    CONSTRAINT okr_child_unit_okr_unit_fkey1 FOREIGN KEY (id) REFERENCES public.okr_unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT okr_child_unit_okr_unit_fkey2 FOREIGN KEY (parent_okr_unit_id) REFERENCES public.okr_unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.okr_company_history
(
    id bigint NOT NULL,
    CONSTRAINT okr_company_history_pkey PRIMARY KEY (id)
) WITH (
      OIDS= FALSE
    );

CREATE TABLE public.okr_company
(
    id         bigint NOT NULL,
    cycle_id   bigint,
    history_id bigint,
    CONSTRAINT company_cycle_fkey FOREIGN KEY (cycle_id)
        REFERENCES public.cycle (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT company_company_history_fkey FOREIGN KEY (history_id)
        REFERENCES public.okr_company_history (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT okr_unit_fkey FOREIGN KEY (id)
        REFERENCES public.okr_unit (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT company_pkey PRIMARY KEY (id)
) WITH (
      OIDS= FALSE
    );

CREATE TABLE public.okr_branch
(
    id                      bigint NOT NULL,
    CONSTRAINT okr_branch_pkey PRIMARY KEY (id),
    CONSTRAINT okr_branch_okr_child_unit_fkey FOREIGN KEY (id)
        REFERENCES public.okr_child_unit (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.okr_department
(
    id                   bigint NOT NULL,
    okr_master_id        uuid,
    okr_topic_sponsor_id uuid,
    CONSTRAINT department_pkey PRIMARY KEY (id),
    CONSTRAINT department_okr_child_unit_fkey FOREIGN KEY (id)
        REFERENCES public.okr_child_unit (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
) WITH (
      OIDS= FALSE
    );

-- objectives & key results

CREATE TABLE public.objective
(
    id                  bigint NOT NULL,
    contact_person_id   character varying(255),
    description         character varying(255),
    name                character varying(255),
    remark              character varying(255),
    review              character varying(255),
    is_active           boolean default true,
    sequence            int     default 0,
    parent_objective_id bigint,
    parent_okr_unit_id bigint,
    CONSTRAINT objective_pkey PRIMARY KEY (id),
    CONSTRAINT objective_okr_unit_fkey FOREIGN KEY (parent_okr_unit_id)
        REFERENCES public.okr_unit (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT objective_objective_fkey FOREIGN KEY (parent_objective_id)
        REFERENCES public.objective (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
      OIDS= FALSE
    );

CREATE TABLE public.key_result
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
        REFERENCES public.objective (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
      OIDS= FALSE
    );

CREATE TABLE public.key_result_milestone
(
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    value bigint NOT NULL,
    parent_key_result_id bigint,
    CONSTRAINT pk_key_result_milestones PRIMARY KEY (id),
    CONSTRAINT fk_key_result_milesones_keyresults FOREIGN KEY (parent_key_result_id) REFERENCES public.key_result (id)
);

CREATE TABLE public.note
(
    id                   bigint    NOT NULL,
    text                 character varying(255),
    date                 timestamp NOT NULL default CURRENT_TIMESTAMP,
    user_id              uuid,
    parent_key_result_id bigint,
    CONSTRAINT note_pkey PRIMARY KEY (id),
    CONSTRAINT note_key_result_fkey FOREIGN KEY (parent_key_result_id)
        REFERENCES public.key_result (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
      OIDS= FALSE
    );

CREATE TABLE public.okr_member
(
    okr_department_id bigint NOT NULL,
    okr_member_id uuid,
    CONSTRAINT okr_member_department_fkey FOREIGN KEY (okr_department_id)
        REFERENCES public.okr_department (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
      OIDS= FALSE
    );

-- users

CREATE TABLE public.admin_user
(
    id uuid NOT NULL,
    PRIMARY KEY (id)
) WITH (
      OIDS = false
    );

CREATE TABLE public.aad_user
(
    id            uuid                   NOT NULL,
    given_name    character varying(255) NOT NULL,
    surname       character varying(255) NOT NULL,
    mail          character varying(255) NOT NULL,
    mail_nickname character varying(255) NOT NULL,
    job_title     character varying(255),
    department    character varying(255),
    photo         TEXT                   NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
) WITH (
      OIDS= FALSE
    );

CREATE TABLE public.local_user
(
    id         uuid NOT NULL,
    given_name character varying NOT NULL,
    surname    character varying NOT NULL,
    mail       character varying UNIQUE NOT NULL,
    job_title  character varying,
    department character varying,
    photo      character varying,
    active     boolean,
    created_at timestamp without time zone default NOW() NOT NULL,
    password   character varying,
    CONSTRAINT local_user_pkey PRIMARY KEY (id)
) WITH (
      OIDS = FALSE
    );

CREATE TABLE public.password_token
(
    email_identifier uuid                        NOT NULL,
    local_user_id    uuid                        NOT NULL,
    created_at       timestamp without time zone NOT NULL,
    CONSTRAINT password_token_pkey PRIMARY KEY (email_identifier)
) WITH (
      OIDS = FALSE
    );

-- logging & configurations

CREATE TABLE public.log
(
    id          BIGINT                 NOT NULL,
    level       CHARACTER VARYING(255) NOT NULL,
    timestamp   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file_name   CHARACTER VARYING(255) NOT NULL,
    line_number CHARACTER VARYING(255) NOT NULL,
    message     TEXT,
    CONSTRAINT log_pkey PRIMARY KEY (id)
);

CREATE TABLE public.init_state
(
    id bigint NOT NULL,
    init_state integer,
    CONSTRAINT init_state_pkey PRIMARY KEY (id)
);

CREATE TABLE public.oauth_client_details
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

CREATE TABLE public.oauth_configuration
(
    key character varying(255) NOT NULL,
    value character varying(255),
    type character varying(255),
    CONSTRAINT pk_oauth_configuration PRIMARY KEY (key)
);

INSERT INTO public.oauth_configuration (key, value, type) VALUES ('client-id', 'clientapp', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('client-secret', '', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('scope', 'USER', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('redirect-uri', '', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('silent-refresh-redirect-uri', '', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('auth-type', 'local', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('issuer', '', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('response-type', 'password,refresh_token', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('oidc', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('show-debug-information', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('require-https', 'false', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('strict-discovery-document-validation', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('use-http-basic-auth', 'true', 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('access-token-uri', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('user-authorization-uri', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('client-authentication-scheme', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('grant-type', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('auto-approve-scopes', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('token-name', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('user-info-uri', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('prefer-token-info', null, 'text');
INSERT INTO public.oauth_configuration (key, value, type) VALUES ('token-endpoint', '/oauth/token', 'text');

CREATE TABLE public.user_settings
(
    id                 bigint NOT NULL,
    user_id            uuid   NOT NULL,
    default_okr_company_id bigint,
    default_team_id    bigint,
    CONSTRAINT user_settings_pkey PRIMARY KEY (id),
    CONSTRAINT user_settings_company_fkey FOREIGN KEY (default_okr_company_id)
        REFERENCES public.okr_company (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT user_settings_team_fkey FOREIGN KEY (default_team_id)
        REFERENCES public.okr_department (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
) WITH (
      OIDS = FALSE
    );

CREATE TABLE public.configuration
(
    id    bigint                 NOT NULL,
    name  character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    type character varying(255),
    PRIMARY KEY (id)
) WITH (
      OIDS = FALSE
    );

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'max-key-results', '7', 'number');

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'objective-progress-green-yellow-threshold', '-0.2', 'number');

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'objective-progress-yellow-red-threshold', '-0.4', 'number');

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'general_frontend-base-url', 'http://localhost:4200', 'text');

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'email_from', 'no-reply@burning-okr.com', 'text');

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'email_subject_new-user', 'Please set your password for BurningOKR', 'text');

INSERT INTO configuration(id, name, value, type)
VALUES (nextval('hibernate_sequence'), 'email_subject_forgot-password', 'Reset your Password for BurningOKR', 'text');

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'email_subject_feedback', 'OKR Tool - Feedback', 'text');

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'feedback_receivers', 'feedback@burning-okr.com', 'text');

INSERT INTO public.configuration (id, name, value, type)
VALUES (nextval('public.hibernate_sequence'), 'topic-sponsors-activated', 'true', 'checkbox');
