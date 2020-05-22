-- Primary key name convention:
-- PK_<TablenameInCamelCase>

--Foreign key name convention:
-- FK_<OwnTableName>_<ForeignTableName>

--If multiple foreign keys reference the same table:
--FK_<OwnTableName>_<ForeignTableName>_<OwnTableColumn>

CREATE TABLE public.aad_user (
    given_name character varying(255) NOT NULL,
    surname character varying(255) NOT NULL,
    mail_nickname character varying(255) NOT NULL,
    id uuid NOT NULL,
    job_title character varying(255),
    department character varying(255),
    photo text,
    mail character varying(255),
    CONSTRAINT PK_AadUser PRIMARY KEY (id)
);


CREATE TABLE public.activity (
    id bigint NOT NULL,
    action integer,
    date timestamp without time zone,
    object character varying(255),
    user_id character varying(255),
    CONSTRAINT PK_Activity PRIMARY KEY (id)
);


CREATE TABLE public.admin_user (
    id uuid NOT NULL,
    CONSTRAINT PK_AdminUser PRIMARY KEY (id)

);


CREATE TABLE public.company_history (
    id bigint NOT NULL,
    CONSTRAINT PK_CompanyHistory PRIMARY KEY (id)
);


CREATE TABLE public.company_structure (
    id bigint NOT NULL,
    label character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT PK_CompanyStructure PRIMARY KEY (id)
);


CREATE TABLE public.configuration (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    type character varying(255),
    CONSTRAINT PK_Configuration PRIMARY KEY (id)
);


CREATE TABLE public.corporate_objective_structure (
    id bigint NOT NULL,
    name character varying(255),
    label character varying(255),
    department_structure_id bigint NOT NULL,
    parent_structure_id bigint,
    CONSTRAINT PK_CorporateObjectiveStructure PRIMARY KEY (id),
    CONSTRAINT FK_CorporateObjectiveStructure_CorporateObjectiveStructure_DepartmentStructureId
      FOREIGN KEY (department_structure_id)
      REFERENCES public.corporate_objective_structure(id),
    CONSTRAINT FK_CorporateObjectiveStructure_CorporateObjectiveStructure_ParentStructureId
      FOREIGN KEY (parent_structure_id)
      REFERENCES public.corporate_objective_structure(id)
);


CREATE TABLE public.cycle (
    id bigint NOT NULL,
    cycle_state integer,
    factual_end_date date,
    factual_start_date date,
    name character varying(255) NOT NULL,
    planned_end_date date NOT NULL,
    planned_start_date date NOT NULL,
    is_visible boolean DEFAULT true,
    CONSTRAINT PK_Cycle PRIMARY KEY (id)
);


CREATE TABLE public.company (
    id bigint NOT NULL,
    cycle_id bigint,
    history_id bigint,
    CONSTRAINT PK_Company PRIMARY KEY (id),
    CONSTRAINT FK_Company_Cycle FOREIGN KEY (cycle_id) REFERENCES public.cycle(id),
    CONSTRAINT FK_Company_CompanyStructure FOREIGN KEY (id) REFERENCES public.company_structure(id),
    CONSTRAINT FK_Company_CompanyHistory FOREIGN KEY (history_id) REFERENCES public.company_history(id)

);

CREATE TABLE public.department (
    id bigint NOT NULL,
    parent_structure_id bigint,
    okr_master_id uuid,
    okr_topic_sponsor_id uuid,
    is_active boolean DEFAULT true,
    CONSTRAINT PK_Department PRIMARY KEY (id),
    CONSTRAINT FK_Department_CompanyStructure_ParentStructureId FOREIGN KEY (parent_structure_id) REFERENCES public.company_structure(id)
      ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_Department_CompanyStructure_Id FOREIGN KEY (id) REFERENCES public.company_structure(id)
      ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE public.init_state (
    id bigint NOT NULL,
    init_state integer,
    CONSTRAINT PK_initState PRIMARY KEY (id)
);


CREATE TABLE public.local_user (
    id uuid NOT NULL,
    given_name character varying NOT NULL,
    surname character varying NOT NULL,
    mail character varying NOT NULL,
    job_title character varying,
    department character varying,
    photo character varying,
    active boolean,
    password character varying,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
     CONSTRAINT PK_LocalUser PRIMARY KEY (id),
     CONSTRAINT LocalUserHasUniqueMail UNIQUE (mail)
);


CREATE TABLE public.log (
    id bigint NOT NULL,
    level character varying(255) NOT NULL,
    "timestamp" timestamp without time zone DEFAULT now(),
    file_name character varying(255) NOT NULL,
    line_number character varying(255) NOT NULL,
    message text,
    CONSTRAINT PK_Log PRIMARY KEY (id)
);



CREATE TABLE public.oauth_client_details (
    client_id character varying(255) NOT NULL,
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
    CONSTRAINT PK_OauthClientDetails PRIMARY KEY (client_id)
);


CREATE TABLE public.oauth_configuration (
    key character varying(255) NOT NULL,
    value character varying(255),
    type character varying(255),
    CONSTRAINT PK_OauthConfiguration PRIMARY KEY (key)
);


CREATE TABLE public.objective (
    id bigint NOT NULL,
    contact_person_id character varying(255),
    description character varying(255),
    name character varying(255),
    remark character varying(255),
    review character varying(255),
    parent_objective_id bigint,
    parent_structure_id bigint,
    is_active boolean DEFAULT true,
    sequence integer DEFAULT 0,
    CONSTRAINT PK_Objective PRIMARY KEY (id),
    CONSTRAINT FK_Objective_CompanyStructure FOREIGN KEY (parent_structure_id) REFERENCES public.company_structure(id),
    CONSTRAINT FK_Objective_Objective FOREIGN KEY (parent_objective_id) REFERENCES public.objective(id)
);


CREATE TABLE public.key_result (
    id bigint NOT NULL,
    current_value bigint DEFAULT 0 NOT NULL,
    description character varying(255),
    name character varying(255),
    start_value bigint DEFAULT 0 NOT NULL,
    target_value bigint DEFAULT 0 NOT NULL,
    unit integer,
    parent_objective_id bigint,
    sequence integer DEFAULT 0,
    CONSTRAINT PK_KeyResult PRIMARY KEY (id),
     CONSTRAINT FK_KeyResult_Objective FOREIGN KEY (parent_objective_id) REFERENCES public.objective(id)
);


CREATE TABLE public.note (
    id bigint NOT NULL,
    text character varying(255),
    user_id uuid,
    parent_key_result_id bigint,
    date timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT PK_Note PRIMARY KEY (id),
    CONSTRAINT FK_Note_KeyResults FOREIGN KEY (parent_key_result_id) REFERENCES public.key_result(id)
);


CREATE TABLE public.okr_member (
    department_id bigint NOT NULL,
    okr_member_id uuid,
    CONSTRAINT FK_OkrMember_Department FOREIGN KEY (department_id) REFERENCES public.department(id)
);


CREATE TABLE public.password_token (
    email_identifier uuid NOT NULL,
    local_user_id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL,
    CONSTRAINT PK_PasswordToken PRIMARY KEY (email_identifier)
);


CREATE TABLE public.user_settings (
    id bigint NOT NULL,
    user_id uuid NOT NULL,
    default_company_id bigint,
    default_team_id bigint,
    CONSTRAINT PK_UserSettings PRIMARY KEY (id),
    CONSTRAINT FK_UserSettings_Company FOREIGN KEY (default_company_id) REFERENCES public.company(id)
      ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_UserSettings_Department FOREIGN KEY (default_team_id) REFERENCES public.department(id)
      ON UPDATE CASCADE ON DELETE CASCADE
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

INSERT INTO public.configuration VALUES (63, 'general_frontend-base-url', 'http://localhost:4200', 'text');
INSERT INTO public.configuration VALUES (65, 'email_from', 'no-reply@burning-okr.com', 'text');
INSERT INTO public.configuration VALUES (67, 'email_subject_new-user', 'Please set your password for BurningOKR', 'text');
INSERT INTO public.configuration VALUES (69, 'email_subject_forgot-password', 'Reset your Password for BurningOKR', 'text');
INSERT INTO public.configuration VALUES (70, 'email_subject_feedback', 'OKR Tool - Feedback', 'text');
INSERT INTO public.configuration VALUES (71, 'feedback_receivers', 'feedback@burning-okr.com', 'text');
INSERT INTO public.configuration VALUES (60, 'max-key-results', '7', 'number');
INSERT INTO public.configuration VALUES (61, 'objective-progress-green-yellow-threshold', '-0.2', 'number');
INSERT INTO public.configuration VALUES (62, 'objective-progress-yellow-red-threshold', '-0.4', 'number');

CREATE SEQUENCE public.hibernate_sequence
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 54
    CACHE 1;
