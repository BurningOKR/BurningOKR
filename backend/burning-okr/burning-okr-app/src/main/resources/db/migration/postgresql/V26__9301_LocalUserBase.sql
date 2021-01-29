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
    date_created character varying,
    password character varying,
    CONSTRAINT local_user_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
