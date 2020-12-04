ALTER TABLE public.aaduser
    ADD COLUMN mail CHARACTER varying(255),
    DROP COLUMN user_principal_name;