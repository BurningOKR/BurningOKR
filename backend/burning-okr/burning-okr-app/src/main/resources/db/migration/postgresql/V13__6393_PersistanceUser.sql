CREATE TABLE public.aaduser
(
    object_id           character(255) NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (object_id),
    given_name          character(255) NOT NULL,
    surname             character(255) NOT NULL,
    user_principal_name character(255) NOT NULL,
    mail_nickname       character(255) NOT NULL
) WITH (
      OIDS= FALSE
    );

ALTER TABLE public.note ADD date date NOT NULL default current_date;
