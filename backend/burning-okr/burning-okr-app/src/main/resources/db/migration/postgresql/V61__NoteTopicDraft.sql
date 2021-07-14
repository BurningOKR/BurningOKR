CREATE TABLE public.note_topic_draft
(
    id bigint NOT NULL
) WITH (OIDS = FALSE);

ALTER TABLE public.note_topic_draft
    ADD text character varying(255),
    ADD user_id character varying(255),
    ADD parent_topic_draft_id bigint,
    ADD date TIMESTAMP,
    ALTER COLUMN date SET DEFAULT CURRENT_TIMESTAMP;
