CREATE TEMPORARY TABLE migration_okr_unit
(
    id        BIGINT PRIMARY KEY NOT NULL,
    label     VARCHAR(255),
    name      VARCHAR(255),
    cycle_id  BIGINT,
    history_id BIGINT,
    is_active BOOL,
    okr_master_id UUID,
    okr_topic_description BIGINT,
    okr_sponsor_id UUID,
    okr_unit_type VARCHAR(31),
    parent_okr_unit_id BIGINT

);

CREATE TEMPORARY TABLE old_okr_unit
(
    id        BIGINT PRIMARY KEY NOT NULL,
    label     VARCHAR(255),
    name      VARCHAR(255),
    cycle_id  BIGINT,
    history_id BIGINT,
    is_active BOOL,
    okr_master_id UUID,
    okr_topic_description BIGINT,
    okr_sponsor_id UUID,
    okr_unit_type VARCHAR(31),
    parent_okr_unit_id BIGINT

);

INSERT INTO migration_okr_unit(id, label, name, cycle_id, history_id, is_active, okr_master_id, okr_topic_description, okr_sponsor_id, okr_unit_type, parent_okr_unit_id) SELECT * FROM okr_unit;

ALTER TABLE okr_unit
    DROP COLUMN history_id;


ALTER TABLE okr_unit
    ADD COLUMN company_history_id BIGINT,
    ADD CONSTRAINT fk_company_history FOREIGN KEY (company_history_id) REFERENCES okr_unit_history (id) ON DELETE CASCADE;

ALTER TABLE okr_unit
    ADD COLUMN branch_history_id BIGINT,
    ADD CONSTRAINT fk_branch_history FOREIGN KEY (branch_history_id) REFERENCES okr_unit_history (id) ON DELETE CASCADE;

ALTER TABLE okr_unit
    ADD COLUMN department_history_id BIGINT,
    ADD CONSTRAINT fk_department_history FOREIGN KEY (department_history_id) REFERENCES okr_unit_history (id) ON DELETE CASCADE;

INSERT INTO okr_unit(id, label, name, cycle_id, is_active, okr_master_id, okr_topic_description_id, okr_topic_sponsor_id, okr_unit_type, parent_okr_unit_id,company_history_id, branch_history_id, department_history_id)
    SELECT id, history_id, okr_unit_type FROM migration_okr_unit mUnit
        JOIN old_okr_unit oldUnit ON oldUnit.id = mUnit.id AND
                                 oldUnit.okr_unit_type = mUnit.okr_unit_type;

DROP TABLE migration_okr_unit;
DROP TABLE old_okr_unit;
