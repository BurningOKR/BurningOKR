CREATE TABLE public.note_key_result (
    id bigint NOT NULL,
    parent_key_result_id bigint NOT NULL,
    CONSTRAINT id_pk PRIMARY KEY (id),
    CONSTRAINT key_result_fk FOREIGN KEY (parent_key_result_id)
        REFERENCES public.key_result (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
     OIDS=FALSE
);

-- Data transfer
INSERT INTO public.note_key_result (
                                    id,
                                    parent_key_result_id)
SELECT id, parent_key_result_id
FROM public.note;


ALTER TABLE public.note
    DROP CONSTRAINT fknak03u9uw631nfor92v3ce4uc,
    DROP COLUMN parent_key_result_id;
