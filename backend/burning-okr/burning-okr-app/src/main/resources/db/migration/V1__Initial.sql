CREATE TABLE public.activity
(
    id bigint NOT NULL,
    action integer,
    date timestamp without time zone,
    object character varying(255),
    user_id character varying(255),
    CONSTRAINT PK_Activity PRIMARY KEY (id)
)WITH (
     OIDS=FALSE
    );


CREATE TABLE public.cycle
(
    id bigint NOT NULL,
    cycle_state integer,
    factual_end_date date,
    factual_start_date date,
    name character varying(255) NOT NULL,
    planned_end_date date NOT NULL,
    planned_start_date date NOT NULL,
    s_visible boolean default true,
    CONSTRAINT PK_Cycle PRIMARY KEY (id)
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.company_history
(
    id bigint NOT NULL,
    CONSTRAINT PK_CompanyHistory PRIMARY KEY (id)
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.company_structure
(
    id bigint NOT NULL,
    label character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT PK_CompanyStructure PRIMARY KEY (id)
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.company
(
    id bigint NOT NULL,
    cycle_id bigint,
    history_id bigint,
    CONSTRAINT PK_Company PRIMARY KEY (id),
    CONSTRAINT FK_Company_Cycle FOREIGN KEY (cycle_id)
      REFERENCES public.cycle (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT FK_Company_CompanyHistory FOREIGN KEY (history_id)
      REFERENCES public.company_history (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT FK_Company_CompanyStructure FOREIGN KEY (id)
        REFERENCES public.company_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.department
(
    id bigint NOT NULL,
    parent_structure_id bigint,
    okr_master_id uuid  USING (okr_master_id::uuid),
    okr_topic_sponsor_id uuid  USING (okr_topic_sponsor_id::uuid),
    CONSTRAINT PK_Department PRIMARY KEY (id),
    CONSTRAINT FK_Department_CompanyStructure_ParentStructureId FOREIGN KEY (parent_structure_id)
      REFERENCES public.company_structure
      ON UPDATE CASCADE ON DELETE CASCADE ,
    CONSTRAINT FK_Department_CompanyStructure_Id FOREIGN KEY (id)
      REFERENCES public.company_structure
      ON UPDATE CASCADE ON DELETE CASCADE
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.objective
(
    id bigint NOT NULL,
    contact_person_id character varying(255),
    description character varying(255),
    name character varying(255),
    remark character varying(255),
    review character varying(255),
    parent_objective_id bigint,
    parent_structure_id bigint,
    is_active boolean default true,
    sequence int default 0,
    CONSTRAINT PK_Objective PRIMARY KEY (id),
    CONSTRAINT FK_Objective_CompanyStructure FOREIGN KEY (parent_structure_id)
      REFERENCES public.company_structure (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT FK_Objective_Objective FOREIGN KEY (parent_objective_id)
      REFERENCES public.objective (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.key_result
(
    id bigint NOT NULL,
    current_value bigint NOT NULL default 0,
    description character varying(255),
    name character varying(255),
    start_value bigint NOT NULL default 0,
    target_value bigint NOT NULL default 0,
    unit integer,
    parent_objective_id bigint,
    sequence int default 0,
    CONSTRAINT PK_KeyResult PRIMARY KEY (id),
    CONSTRAINT FK_KeyResult_Objective FOREIGN KEY (parent_objective_id)
      REFERENCES public.objective (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.note
(
    id bigint NOT NULL,
    text character varying(255),
    user_id uuid USING (user_id::uuid),
    parent_key_result_id bigint,
    date TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
    CONSTRAINT PK_Note PRIMARY KEY (id),
    CONSTRAINT FK_Note_KeyResult FOREIGN KEY (parent_key_result_id)
      REFERENCES public.key_result (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.okr_member
(
    department_id bigint NOT NULL,
    okr_member_id uuid USING (okr_member_id::uuid),
    okr_master_id character varying(255),
    okr_topic_sponsor_id character varying(255),
    is_active boolean default true,
    CONSTRAINT FK_OkrMember_Department FOREIGN KEY (department_id)
        REFERENCES public.department (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.admin_user
(
    id uuid NOT NULL,
    CONSTRAINT PK_AdminUser PRIMARY KEY (id)
)
    WITH (
        OIDS = false
    );


CREATE TABLE public.configuration
(
    id    bigint NOT NULL,
    name  character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    CONSTRAINT PK_Configuration PRIMARY KEY (id)
)
    WITH (
        OIDS= FALSE
    );


CREATE TABLE public.user_settings
(
    id bigint NOT NULL,
    user_id uuid USING (user_id::uuid),
    default_company_id bigint,
    default_team_id    bigint,
    CONSTRAINT PK_UserSettings PRIMARY KEY (id),
    CONSTRAINT FK_UserSettings_Company FOREIGN KEY (default_company_id)
        REFERENCES public.companyMATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_UserSEttings_Department FOREIGN KEY (default_team_id)
        REFERENCES public.department MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
)
    WITH (
        OIDS= FALSE
    );


CREATE TABLE public.aad_user
(
    id  uuid USING (id::uuid),
    given_name          CHARACTER VARYING (255) NOT NULL,
    surname             CHARACTER VARYING (255) NOT NULL,
    mail_nickname       CHARACTER VARYING (255) NOT NULL,
    ail CHARACTER varying(255),
    job_title CHARACTER VARYING (255),
    department CHARACTER VARYING (255),
    photo TEXT NULL,

    CONSTRAINT PK_AadUser PRIMARY KEY (id)
) WITH (
      OIDS= FALSE
    );


CREATE TABLE public.log (
    id BIGINT NOT NULL,
    level CHARACTER VARYING(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file_name CHARACTER VARYING(255) NOT NULL,
    line_number CHARACTER VARYING(255) NOT NULL,
    message TEXT,
    CONSTRAINT PK_Log PRIMARY KEY (id)
) WITH (
      OIDS = FALSE
    );


CREATE TABLE public.corporate_objective_structure(
    id bigint NOT NULL,
    name character varying(255),
    label character varying(255),
    department_structure_id bigint NOT NULL,
    parent_structure_id BIGINT,
    CONSTRAINT PL_CorporateObjectiveStructure PRIMARY KEY (id),
    CONSTRAINT FK_CorporateObjectiveStructure_CorporateObjectiveStructure_Department FOREIGN KEY (department_structure_id)
        REFERENCES public.corporate_objective_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT FK_CorporateObjectiveStructure_CorporateObjectiveStructure_ParentStructure FOREIGN KEY (parent_structure_id)
        REFERENCES public.corporate_objective_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.local_user
(
    id uuid NOT NULL,
    given_name character varying,
    surname character varying,
    mail character varying,
    job_title character varying,
    department character varying,
    photo character varying,
    active boolean,
    password character varying,
    created_at timestamp without time zone,
    CONSTRAINT PK_LocalUser PRIMARY KEY (id),
    ADD CONSTRAINT local_user_unique_email UNIQUE (mail)
)
WITH (
    OIDS = FALSE
)

CREATE TABLE public.password_token
(
    email_identifier uuid NOT NULL,
    local_user_id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL,
    CONSTRAINT password_token_pkey PRIMARY KEY (email_identifier)
)
    WITH (
        OIDS = FALSE
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
    CONSTRAING PK_OauthClientDetails PRIMARY KEY (client_id)
);


CREATE SEQUENCE public.hibernate_sequence
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 54
    CACHE 1;

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'max-key-results', '7');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'objective-progress-green-yellow-threshold', '-0.2');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'objective-progress-yellow-red-threshold', '-0.4');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'general_frontend-base-url', 'http://localhost:4200');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'initFlag', '');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'passwords_new-user_email_from', 'no-reply@burning-okr.com');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'passwords_new-user_email_template-name', 'set-password.html');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'passwords_new-user_email_subject', 'Please set your password for BurningOKR');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'passwords_new-user_email_url-suffix', '/auth/setpassword');