CREATE TABLE public.okr_topic_draft
(
    id bigint NOT NULL,
    parent_unit_id bigint,
    CONSTRAINT pk_topic_draft_id PRIMARY KEY(id),
    CONSTRAINT fk_topic_draft_parent_unit_id FOREIGN KEY(parent_unit_id) REFERENCES okr_unit(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT fk_topic_draft_id FOREIGN KEY(id) REFERENCES okr_topic_description(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)

