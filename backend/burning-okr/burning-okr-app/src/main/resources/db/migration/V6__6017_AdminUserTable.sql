CREATE TABLE public.admin_user
(
    id uuid NOT NULL,
    PRIMARY KEY (id)
) WITH (OIDS = false);

ALTER TABLE public.admin_user
OWNER TO admin;