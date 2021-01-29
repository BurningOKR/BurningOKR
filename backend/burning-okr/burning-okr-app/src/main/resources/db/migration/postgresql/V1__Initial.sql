CREATE TABLE public.activity
(
    id bigint NOT NULL,
    CONSTRAINT activity_pkey PRIMARY KEY (id)
)WITH (
     OIDS=FALSE
    );


CREATE TABLE public.company
(
    id bigint NOT NULL,
    CONSTRAINT company_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.company_history
(
    id bigint NOT NULL,
    CONSTRAINT company_history_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS=FALSE
    );


CREATE TABLE public.company_structure
(
    id bigint NOT NULL,
    label character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT company_structure_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE public.cycle
(
    id bigint NOT NULL,
    cycle_state integer,
    factual_end_date date,
    factual_start_date date,
    name character varying(255) NOT NULL,
    planned_end_date date NOT NULL,
    planned_start_date date NOT NULL,
    CONSTRAINT cycle_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE public.department
(
    id bigint NOT NULL
)
    WITH (
        OIDS=FALSE
    );

ALTER TABLE public.activity
    ADD action integer,
    ADD date timestamp without time zone,
    ADD object character varying(255),
    ADD user_id character varying(255);

CREATE TABLE public.gruppen
(
    id bigint NOT NULL
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE public.key_result
(
    id bigint NOT NULL
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE public.note
(
    id bigint NOT NULL
)
    WITH (
        OIDS=FALSE
    );

CREATE TABLE public.objective
(
    id bigint NOT NULL
)
    WITH (
        OIDS=FALSE
    );

ALTER TABLE public.company
    ADD cycle_id bigint,
    ADD history_id bigint,
    ADD CONSTRAINT fk7atk1eyrvucavbg2c3s9gs05c FOREIGN KEY (cycle_id)
        REFERENCES public.cycle (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    ADD CONSTRAINT fkkp8tlodqoths7ersfi77gepvw FOREIGN KEY (history_id)
        REFERENCES public.company_history (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    ADD CONSTRAINT fkp9450spu2659bdjr9efnhp02e FOREIGN KEY (id)
        REFERENCES public.company_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE public.department
    ADD permit_okrs boolean NOT NULL default true,
    ADD parent_structure_id bigint,
    ADd CONSTRAINT department_pkey PRIMARY KEY (id),
    ADD CONSTRAINT fkbqdg50hket4gx9j9xixvew31o FOREIGN KEY (parent_structure_id)
        REFERENCES public.company_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    ADD CONSTRAINT fktqv08tc4aufiv4bu0kwcj185q FOREIGN KEY (id)
        REFERENCES public.company_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.gruppen
    ADD parent_structure_id bigint,
    ADD CONSTRAINT gruppen_pkey PRIMARY KEY (id),
    ADD CONSTRAINT fkch0rxhdc4q1n8f2tqj0dq973 FOREIGN KEY (id)
        REFERENCES public.company_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    ADD CONSTRAINT fkhm4h6o6nwe8myywb9hgq25s07 FOREIGN KEY (parent_structure_id)
        REFERENCES public.company_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE public.objective
    ADD contact_person_id character varying(255),
    ADD description character varying(255),
    ADD name character varying(255),
    ADD remark character varying(255),
    ADD review character varying(255),
    ADD parent_objective_id bigint,
    ADD parent_structure_id bigint,
    ADD CONSTRAINT objective_pkey PRIMARY KEY (id),
    ADD CONSTRAINT fkiwxcgedb2ey0wpsfoy842amos FOREIGN KEY (parent_structure_id)
        REFERENCES public.company_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    ADd CONSTRAINT fksq3x9rkdop29lyub1to7pr9rc FOREIGN KEY (parent_objective_id)
        REFERENCES public.objective (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE public.key_result
    ADD current_value bigint NOT NULL default 0,
    ADD description character varying(255),
    ADD name character varying(255),
    ADD start_value bigint NOT NULL default 0,
    ADD target_value bigint NOT NULL default 0,
    ADD unit integer,
    ADD parent_objective_id bigint,
    ADD CONSTRAINT key_result_pkey PRIMARY KEY (id),
    ADD CONSTRAINT fk1ctsydhbbgrw28k67clcwvev9 FOREIGN KEY (parent_objective_id)
        REFERENCES public.objective (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE public.note
    ADD text character varying(255),
    ADD user_id character varying(255),
    ADD parent_key_result_id bigint,
    ADD CONSTRAINT note_pkey PRIMARY KEY (id),
    ADD CONSTRAINT fknak03u9uw631nfor92v3ce4uc FOREIGN KEY (parent_key_result_id)
        REFERENCES public.key_result (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE SEQUENCE public.hibernate_sequence
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 54
    CACHE 1;
