CREATE TABLE okr_topic_draft_history
(
    id bigint NOT NULL,
    CONSTRAINT pk_okr_topic_draft_history PRIMARY KEY(id)
);

ALTER TABLE okr_topic_draft ADD history_id bigint;
GO

UPDATE okr_topic_draft SET history_id = NEXT VALUE FOR hibernate_sequence;
GO

INSERT INTO okr_topic_draft_history(id) (SELECT history_id FROM okr_topic_draft);

ALTER TABLE okr_topic_draft ADD FOREIGN KEY (history_id)
    REFERENCES okr_topic_draft_history (id)
    ON UPDATE NO ACTION ON DELETE NO ACTION;