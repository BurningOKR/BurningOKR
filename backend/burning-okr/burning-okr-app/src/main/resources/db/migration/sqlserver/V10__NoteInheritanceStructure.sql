-- TODO needs to be tested
ALTER TABLE note DROP CONSTRAINT note_key_result_fkey;

ALTER TABLE note DROP COLUMN parent_key_result_id;

CREATE TABLE note_key_result (
    id bigint NOT NULL,
    parent_key_result_id bigint NOT NULL,
    CONSTRAINT id_pk PRIMARY KEY (id),
    CONSTRAINT key_result_fk FOREIGN KEY (parent_key_result_id)
        REFERENCES key_result (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE note_topic_draft (
     id bigint NOT NULL,
     parent_topic_draft bigint NOT NULL,
     CONSTRAINT note_id_pk PRIMARY KEY (id),
     CONSTRAINT topic_draft_fk FOREIGN KEY (parent_topic_draft)
         REFERENCES okr_topic_draft (id)
         ON UPDATE NO ACTION ON DELETE NO ACTION
);
