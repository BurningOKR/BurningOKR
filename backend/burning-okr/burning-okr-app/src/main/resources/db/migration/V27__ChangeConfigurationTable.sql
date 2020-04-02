DELETE FROM public.configuration
	WHERE true;
ALTER TABLE public.configuration DROP COLUMN value;

ALTER TABLE public.configuration
    ADD COLUMN value character varying(255) NOT NULL;

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'max_key_results', '7');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'objective_progress_green_yellow_threshold', '-0.2');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'objective_progress_yellow_red_threshold', '-0.4');
