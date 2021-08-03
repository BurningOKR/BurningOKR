CREATE TABLE note_objective
(
    id               bigint NOT NULL,
    parent_objective_id bigint NOT NULL,
    CONSTRAINT note_objective_id_pk PRIMARY KEY (id),
    CONSTRAINT objective_fk FOREIGN KEY (parent_objective_id)
        REFERENCES objective (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);