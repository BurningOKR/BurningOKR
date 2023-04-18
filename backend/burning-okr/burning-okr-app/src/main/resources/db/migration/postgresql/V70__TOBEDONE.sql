-- Migrate okr_unit to InheritanceType.SINGLE_TABLE
ALTER TABLE okr_unit
    ADD cycle_id BIGINT;

ALTER TABLE okr_unit
    ADD history_id BIGINT;

ALTER TABLE okr_unit
    ADD is_active BOOLEAN;

ALTER TABLE okr_unit
    ADD okr_master_id uuid;

ALTER TABLE okr_unit
    ADD okr_topic_description_id BIGINT;

ALTER TABLE okr_unit
    ADD okr_topic_sponsor_id uuid;

ALTER TABLE okr_unit
    ADD okr_unit_type VARCHAR(31);

ALTER TABLE okr_unit
    ADD parent_okr_unit_id BIGINT;

ALTER TABLE okr_unit
    ADD CONSTRAINT fk_okrunit_on_cycle FOREIGN KEY (cycle_id) REFERENCES cycle (id);

ALTER TABLE okr_unit
    ADD CONSTRAINT fk_okrunit_on_history FOREIGN KEY (history_id) REFERENCES okr_unit_history (id);

ALTER TABLE okr_unit
    ADD CONSTRAINT fk_okrunit_on_okrtopicdescription FOREIGN KEY (okr_topic_description_id) REFERENCES okr_topic_description (id);

ALTER TABLE okr_unit
    ADD CONSTRAINT fk_okrunit_on_parentokrunit FOREIGN KEY (parent_okr_unit_id) REFERENCES okr_unit (id);

ALTER TABLE okr_unit_history
    ADD okr_unit_history_type VARCHAR(31);

-- Migrate task_board table
ALTER TABLE task_board
    ADD CONSTRAINT fk_taskboard_on_parent_unit FOREIGN KEY (parent_unit_id) REFERENCES okr_unit (id);

ALTER TABLE task_board
    DROP CONSTRAINT task_board_parent_unit_fkey;

-- Migrate user_settings table
ALTER TABLE user_settings
    DROP CONSTRAINT default_company_fkey;

ALTER TABLE user_settings
    DROP CONSTRAINT default_team_fkey;

ALTER TABLE user_settings
    ADD CONSTRAINT fk_usersettings_on_defaultokrcompany FOREIGN KEY (default_okr_company_id) REFERENCES okr_unit (id);

ALTER TABLE user_settings
    ADD CONSTRAINT fk_usersettings_on_defaultteam FOREIGN KEY (default_team_id) REFERENCES okr_unit (id);

-- Migrate okr_member table
ALTER TABLE okr_member
    ADD CONSTRAINT fk_okr_member_on_okr_department FOREIGN KEY (okr_department_id) REFERENCES okr_unit (id);

ALTER TABLE okr_member
    DROP CONSTRAINT fk5c2gx62q2posf5ev6d8ctmh71;

-- Insert dummy-values
UPDATE okr_unit_history
SET okr_unit_history_type = 'OkrUnitHistory'
WHERE okr_unit_history_type IS NULL;

UPDATE okr_unit
SET okr_unit_type = 'OkrUnit'
WHERE okr_unit_type IS NULL;

-- TODO create temp_okr_unit-table
CREATE TABLE temp_okr_unit
(
    id                       bigint primary key,
    label                    varchar,
    name                     varchar,
    cycle_id                 bigint,
    history_id               bigint,
    is_active                boolean,
    okr_master_id            uuid,
    okr_topic_description_id bigint,
    okr_topic_sponsor_id     uuid,
    okr_unit_type            varchar,
    parent_okr_unit_id       bigint
);

-- fill temp_okr_unit-table with departments
INSERT INTO temp_okr_unit (id, label, name, okr_master_id, okr_topic_description_id, history_id, parent_okr_unit_id, is_active, okr_unit_type)
SELECT ou.id, label, name, od.okr_master_id, od.okr_topic_description_id, od.history_id, ocu.parent_okr_unit_id, ocu.is_active, 'OKR_DEPARTMENT'
FROM okr_unit ou JOIN okr_child_unit ocu on ou.id = ocu.id JOIN okr_department od on ocu.id = od.id;

-- fill temp_okr_unit-table with branches
INSERT INTO temp_okr_unit (id, label, name, history_id, parent_okr_unit_id, is_active, okr_unit_type)
SELECT ou.id, label, name, ob.history_id, ocu.parent_okr_unit_id, ocu.is_active, 'OKR_BRANCH'
FROM okr_unit ou JOIN okr_child_unit ocu on ou.id = ocu.id JOIN okr_branch ob on ocu.id = ob.id;

-- fill temp_okr_unit-table with companies
INSERT INTO temp_okr_unit (id, label, name, cycle_id, history_id, okr_unit_type)
SELECT ou.id, label, name, oc.cycle_id, oc.history_id, 'OKR_COMPANY'
FROM okr_unit ou JOIN okr_company oc on ou.id = oc.id;

-- TODO copy values from temp_okr_unit-table to okr_unit-table
TRUNCATE okr_unit CASCADE;
INSERT INTO okr_unit (id, label, name, cycle_id, history_id, is_active, okr_master_id, okr_topic_description_id,
                      okr_topic_sponsor_id, okr_unit_type, parent_okr_unit_id)
SELECT *
FROM temp_okr_unit;

-- DROP TABLE temp_okr_unit;

-- TODO migrate okr_unit_history-table

-- TODO drop unused tables
/*DROP TABLE okr_branch CASCADE;

DROP TABLE okr_branch_history CASCADE;

DROP TABLE okr_child_unit CASCADE;

DROP TABLE okr_company CASCADE;

DROP TABLE okr_company_history CASCADE;

DROP TABLE okr_department CASCADE;

DROP TABLE okr_department_history CASCADE;*/