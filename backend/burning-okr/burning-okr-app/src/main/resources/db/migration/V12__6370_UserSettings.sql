CREATE TABLE public.user_settings
(
    id                 bigint                 NOT NULL,
    CONSTRAINT user_settings_pkey PRIMARY KEY (id),
    user_id             character varying(255) NOT NULL,
    default_company_id bigint,
    CONSTRAINT default_company_fkey FOREIGN KEY (default_company_id)
        REFERENCES public.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    default_team_id    bigint,
    CONSTRAINT default_team_fkey FOREIGN KEY (default_team_id)
        REFERENCES public.department (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
    WITH (
        OIDS= FALSE
    );

ALTER TABLE public.user_settings
    OWNER TO admin;