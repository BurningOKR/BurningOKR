CREATE TABLE public.configuration
(
    id    bigint                 NOT NULL,
    name  character varying(255) NOT NULL,
    value float                  NOT NULL,
    PRIMARY KEY (id)
)
    WITH (
        OIDS= FALSE
    );
ALTER TABLE public.configuration
    OWNER TO admin;

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'max_key_results', 7);

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'objective_progress_green_yellow_threshold', -0.2);

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'objective_progress_yellow_red_threshold', -0.4);