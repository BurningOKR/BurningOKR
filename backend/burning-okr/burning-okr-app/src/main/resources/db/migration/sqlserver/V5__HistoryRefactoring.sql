CREATE TABLE okr_unit_history
(
    id bigint NOT NULL,
    CONSTRAINT pk_okr_unit_history PRIMARY KEY(id)
);


CREATE TABLE okr_branch_history
(
    id bigint NOT NULL,
    CONSTRAINT pk_okr_branch_history PRIMARY KEY(id)
);

CREATE TABLE okr_department_history
(
    id bigint NOT NULL,
    CONSTRAINT pk_okr_department_history PRIMARY KEY(id)
);


ALTER TABLE okr_department ADD history_id bigint;
ALTER TABLE okr_branch ADD history_id bigint;
GO


INSERT INTO okr_unit_history (id) (SELECT id FROM okr_company_history);

UPDATE okr_department SET history_id = NEXT VALUE FOR hibernate_sequence;
UPDATE okr_branch SET history_id = NEXT VALUE FOR hibernate_sequence;
GO


INSERT INTO okr_department_history(id) (SELECT history_id FROM okr_department);
INSERT INTO okr_branch_history(id) (SELECT history_id FROM okr_branch);

INSERT INTO okr_unit_history(id) (SELECT id FROM okr_department_history);
INSERT INTO okr_unit_history(id) (SELECT id FROM okr_branch_history);



ALTER TABLE okr_company_history ADD FOREIGN KEY (id)
    REFERENCES okr_unit_history (id)
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_department ADD FOREIGN KEY (history_id)
   REFERENCES okr_department_history (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_department_history ADD FOREIGN KEY (id)
    REFERENCES okr_unit_history (id)
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_branch ADD FOREIGN KEY (history_id)
    REFERENCES okr_branch_history (id)
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_branch_history ADD FOREIGN KEY (id)
    REFERENCES okr_unit_history (id)
    ON UPDATE NO ACTION ON DELETE NO ACTION;