CREATE TABLE public.note_objective
(
    id               bigint NOT NULL,
    parent_objective bigint NOT NULL,
    CONSTRAINT note_objective_id_pk PRIMARY KEY (id),
    CONSTRAINT objective_fk FOREIGN KEY (parent_objective)
        REFERENCES public.objective (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
      OIDS= FALSE
    );