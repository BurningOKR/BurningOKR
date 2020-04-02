ALTER TABLE public.local_user DROP COLUMN date_created;

ALTER TABLE public.local_user
    ADD COLUMN "created_at" timestamp without time zone;

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

ALTER TABLE public.local_user
    ADD CONSTRAINT local_user_unique_email UNIQUE (mail);
