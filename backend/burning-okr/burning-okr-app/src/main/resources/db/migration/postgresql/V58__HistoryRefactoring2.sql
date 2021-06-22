CREATE TABLE okr_topic_draft_history
(
    id bigint NOT NULL,
    CONSTRAINT pk_okr_topic_draft_history PRIMARY KEY(id)
);

ALTER TABLE okr_topic_draft ADD COLUMN history_id bigint;

UPDATE public.okr_topic_draft SET history_id = nextval('hibernate_sequence');

INSERT INTO public.okr_topic_draft_history(id) (SELECT history_id FROM public.okr_topic_draft);

ALTER TABLE okr_topic_draft ADD CONSTRAINT fk_okr_topic_draft_history FOREIGN KEY (history_id)
    REFERENCES public.okr_topic_draft_history (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;