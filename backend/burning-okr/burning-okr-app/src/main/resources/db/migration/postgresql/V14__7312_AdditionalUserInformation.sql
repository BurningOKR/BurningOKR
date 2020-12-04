ALTER TABLE public.aaduser
        ADD COLUMN id CHARACTER VARYING (255),
        ADD COLUMN job_title CHARACTER VARYING (255),
        ADD COLUMN department CHARACTER VARYING (255);

UPDATE public.aaduser SET id=object_id;

ALTER TABLE public.aaduser
        DROP CONSTRAINT user_pkey,
        ADD CONSTRAINT user_pkey PRIMARY KEY(id),
        DROP COLUMN object_id;

ALTER TABLE public.aaduser
        ALTER COLUMN given_name TYPE CHARACTER VARYING (255),
        ALTER COLUMN surname TYPE CHARACTER VARYING (255),
        ALTER COLUMN user_principal_name TYPE CHARACTER VARYING (255),
        ALTER COLUMN mail_nickname TYPE CHARACTER VARYING (255);

ALTER TABLE public.aaduser ADD COLUMN photo TEXT NULL;