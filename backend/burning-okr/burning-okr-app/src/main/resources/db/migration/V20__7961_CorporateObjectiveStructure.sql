CREATE TABLE public.corporate_objective_structure(
    id bigint NOT NULL,
    CONSTRAINT corporate_objective_structure_pkey PRIMARY KEY (id),
    name character varying(255),
    label character varying(255),
    department_structure_id bigint NOT NULL,
    parent_structure_id BIGINT,
    CONSTRAINT department_structure_fkey FOREIGN KEY (department_structure_id)
        REFERENCES public.corporate_objective_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT parent_structure_fkey FOREIGN KEY (parent_structure_id)
        REFERENCES public.corporate_objective_structure (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);
