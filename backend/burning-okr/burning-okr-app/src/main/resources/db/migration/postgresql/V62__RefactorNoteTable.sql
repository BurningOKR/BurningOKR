ALTER TABLE public.note
    DROP CONSTRAINT fknak03u9uw631nfor92v3ce4uc,
    DROP COLUMN parent_key_result_id;

CREATE TABLE public.note_key_result (
    note_id bigint NOT NULL,
    parent_key_result_id bigint NOT NULL,
    CONSTRAINT note_id_pk PRIMARY KEY (note_id),
    CONSTRAINT key_result_fk FOREIGN KEY (parent_key_result_id)
        REFERENCES public.key_result (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
     OIDS=FALSE
);


