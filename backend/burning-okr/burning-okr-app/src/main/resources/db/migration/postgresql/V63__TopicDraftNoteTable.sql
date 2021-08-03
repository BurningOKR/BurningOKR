CREATE TABLE public.note_topic_draft (
    id bigint NOT NULL,
    parent_topic_draft bigint NOT NULL,
    CONSTRAINT note_id_pk PRIMARY KEY (id),
    CONSTRAINT topic_draft_fk FOREIGN KEY (parent_topic_draft)
        REFERENCES public.okr_topic_draft (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
    OIDS=FALSE
);
