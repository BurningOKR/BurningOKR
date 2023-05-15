CREATE TABLE [user]
(
    id         uniqueidentifier NOT NULL,
    given_name VARCHAR,
    surname    VARCHAR,
    mail       VARCHAR,
    job_title  VARCHAR,
    department VARCHAR,
    photo      VARCHAR,
    active     BIT              NOT NULL,
    admin      BIT              NOT NULL,
    created_at date default CURRENT_TIMESTAMP,
    CONSTRAINT pk_user PRIMARY KEY (id)
);
Go

-- Drop unused tables
DROP TABLE admin_user;
DROP TABLE auditor_user;
DROP TABLE password_token;

GO

-- migrate aad_user-table
INSERT INTO [user] (id, given_name, surname, mail, job_title, department, photo, active, admin, created_at)
SELECT id,
       given_name,
       surname,
       mail,
       job_title,
       department,
       photo,
       1,
       0,
       CURRENT_TIMESTAMP
FROM aad_user;
Go

-- migrate local_user-table and add deprecated to name for indicating, that local user can't be used anymore and need to
-- be manually migrated
INSERT INTO [user] (id, given_name, surname, mail, job_title, department, photo, active, admin, created_at)
SELECT id,
       given_name,
       surname + ' DEPRECATED',
       mail,
       job_title,
       department,
       photo,
       active,
       0,
       created_at
FROM local_user;
Go

-- Drop old user-tables
DROP TABLE aad_user;
DROP TABLE local_user;
Go


-----------

-- Migrate okr_unit to InheritanceType.SINGLE_TABLE
ALTER TABLE okr_unit
    ADD cycle_id BIGINT;

ALTER TABLE okr_unit
    ADD history_id BIGINT;

ALTER TABLE okr_unit
    ADD is_active BIT;

ALTER TABLE okr_unit
    ADD okr_master_id uniqueidentifier;

ALTER TABLE okr_unit
    ADD okr_topic_description_id BIGINT;

ALTER TABLE okr_unit
    ADD okr_topic_sponsor_id uniqueidentifier;

ALTER TABLE okr_unit
    ADD okr_unit_type VARCHAR(31);

ALTER TABLE okr_unit
    ADD parent_okr_unit_id BIGINT;
Go

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
Go

-- Migrate user_settings table
ALTER TABLE user_settings
    DROP CONSTRAINT user_settings_company_fkey;

ALTER TABLE user_settings
    DROP CONSTRAINT user_settings_team_fkey;

ALTER TABLE user_settings
    ADD CONSTRAINT fk_usersettings_on_defaultokrcompany FOREIGN KEY (default_okr_company_id) REFERENCES okr_unit (id);

ALTER TABLE user_settings
    ADD CONSTRAINT fk_usersettings_on_defaultteam FOREIGN KEY (default_team_id) REFERENCES okr_unit (id);
Go

-- temp-drop constraints
alter table objective
    drop constraint objective_okr_unit_fkey;
Go

-- Insert dummy-values
UPDATE okr_unit_history
SET okr_unit_history_type = 'OkrUnitHistory'
WHERE okr_unit_history_type IS NULL;

UPDATE okr_unit
SET okr_unit_type = 'OkrUnit'
WHERE okr_unit_type IS NULL;
Go

-- create temp_okr_unit-table
CREATE TABLE temp_okr_unit
(
    id                       bigint primary key,
    label                    varchar,
    name                     varchar,
    cycle_id                 bigint,
    history_id               bigint,
    is_active                BIT,
    okr_master_id            uniqueidentifier,
    okr_topic_description_id bigint,
    okr_topic_sponsor_id     uniqueidentifier,
    okr_unit_type            varchar(31),
    parent_okr_unit_id       bigint
);
Go

-- fill temp_okr_unit-table with departments
INSERT INTO temp_okr_unit (id, label, name, okr_master_id, okr_topic_description_id, history_id, parent_okr_unit_id,
                           is_active, okr_unit_type)
SELECT ou.id,
       label,
       name,
       od.okr_master_id,
       od.okr_topic_description_id,
       od.history_id,
       ocu.parent_okr_unit_id,
       ocu.is_active,
       'OKR_DEPARTMENT'
FROM okr_unit ou
         JOIN okr_child_unit ocu on ou.id = ocu.id
         JOIN okr_department od on ocu.id = od.id;
Go

-- fill temp_okr_unit-table with branches
INSERT INTO temp_okr_unit (id, label, name, history_id, parent_okr_unit_id, is_active, okr_unit_type)
SELECT ou.id, label, name, ob.history_id, ocu.parent_okr_unit_id, ocu.is_active, 'OKR_BRANCH'
FROM okr_unit ou
         JOIN okr_child_unit ocu on ou.id = ocu.id
         JOIN okr_branch ob on ocu.id = ob.id;
Go

-- fill temp_okr_unit-table with companies
INSERT INTO temp_okr_unit (id, label, name, cycle_id, history_id, okr_unit_type)
SELECT ou.id, label, name, oc.cycle_id, oc.history_id, 'OKR_COMPANY'
FROM okr_unit ou
         JOIN okr_company oc on ou.id = oc.id;
Go

-- drop unused tables
DROP TABLE okr_branch;
Go

DROP TABLE okr_branch_history;
Go

DROP TABLE okr_company;
Go

DROP TABLE okr_company_history;
Go

-- drop constraints pointing on child_unit and drop okr_child_unit
ALTER TABLE okr_department
    DROP CONSTRAINT department_okr_child_unit_fkey;
Go

DROP TABLE okr_child_unit;
Go

-- drop constraints pointing on okr_department and drop okr_department
ALTER TABLE okr_member
    DROP CONSTRAINT okr_member_department_fkey;
Go

ALTER TABLE task_board
    DROP CONSTRAINT task_board_parent_unit_fkey;
Go

DROP TABLE okr_department;
Go

-- drop constraints pointing on okr_department_history and drop okr_department_history
DROP TABLE okr_department_history;
Go

-- drop constraints pointing to okr_unit
ALTER TABLE okr_topic_draft
    DROP CONSTRAINT fk_topic_draft_parent_unit_id;
ALTER TABLE user_settings
    DROP CONSTRAINT fk_usersettings_on_defaultokrcompany;
ALTER TABLE user_settings
    DROP CONSTRAINT fk_usersettings_on_defaultteam;
Go

-- copy values from temp_okr_unit-table to okr_unit-table
TRUNCATE table okr_unit;
Go

INSERT INTO okr_unit (id, label, name, cycle_id, history_id, is_active, okr_master_id, okr_topic_description_id,
                      okr_topic_sponsor_id, okr_unit_type, parent_okr_unit_id)
SELECT *
FROM temp_okr_unit;
Go

-- DROP TABLE temp_okr_unit;
DROP TABLE temp_okr_unit;
Go

-- migrate okr_unit_history-table
CREATE TABLE okr_unit_history_temp
(
    id                    bigint primary key,
    okr_unit_history_type varchar(31)
);
Go

INSERT INTO okr_unit_history_temp (id, okr_unit_history_type)
SELECT id, okr_unit_history_type_case
FROM (SELECT ou.okr_unit_type,
             ouh.id,
             CASE
                 WHEN ou.okr_unit_type = 'OKR_DEPARTMENT' THEN 'OKR_DEPARTMENT_HISTORY'
                 WHEN ou.okr_unit_type = 'OKR_BRANCH' THEN 'OKR_BRANCH_HISTORY'
                 WHEN ou.okr_unit_type = 'OKR_COMPANY' THEN 'OKR_COMPANY_HISTORY'
                 END okr_unit_history_type_case
      from okr_unit ou
               JOIN okr_unit_history ouh on ou.history_id = ouh.id) as oo;
Go

UPDATE okr_unit_history
SET okr_unit_history_type = okr_unit_history_temp.okr_unit_history_type
FROM okr_unit_history_temp
WHERE okr_unit_history_temp.id = okr_unit_history.id;
Go

DROP TABLE okr_unit_history_temp;
Go

-- Migrate task_board table
ALTER TABLE task_board
    ADD CONSTRAINT fk_taskboard_on_parent_unit FOREIGN KEY (parent_unit_id) REFERENCES okr_unit (id);
Go

-- Migrate okr_member table
ALTER TABLE okr_member
    ADD CONSTRAINT fk_okr_member_on_okr_department FOREIGN KEY (okr_department_id) REFERENCES okr_unit (id);
Go

-- recreate temp-dropped constraints
alter table objective
    add constraint fk_objective_on_parent_okr_unit
        foreign key (parent_okr_unit_id) references okr_unit;
Go

ALTER TABLE okr_topic_draft
    ADD CONSTRAINT fk_topic_draft_on_parent_unit_id
        foreign key (parent_unit_id) references okr_unit;
Go

ALTER TABLE user_settings
    ADD CONSTRAINT fk_user_settings_on_default_company
        foreign key (default_okr_company_id) references okr_unit;
Go

ALTER TABLE user_settings
    ADD CONSTRAINT fk_user_settings_on_default_team
        foreign key (default_team_id) references okr_unit;
Go

-------------
-- General missing constraints
ALTER TABLE dashboard_creation
    ALTER COLUMN company_id bigint NOT NULL;

ALTER TABLE dashboard_creation
    ALTER COLUMN creator_id uniqueidentifier NOT NULL;

ALTER TABLE key_result_history
    ALTER COLUMN current_value bigint NOT NULL;

ALTER TABLE objective
    ALTER COLUMN is_active bit NOT NULL;

ALTER TABLE cycle
    ALTER COLUMN is_visible bit NOT NULL;

ALTER TABLE key_result_history
    ALTER COLUMN key_result_id bigint NOT NULL;

ALTER TABLE key_result
    ALTER COLUMN sequence int NOT NULL;

ALTER TABLE objective
    ALTER COLUMN sequence int NOT NULL;

ALTER TABLE key_result_history
    ALTER COLUMN start_value bigint NOT NULL;

ALTER TABLE key_result_history
    ALTER COLUMN target_value bigint NOT NULL;

ALTER TABLE task
    ALTER COLUMN task_state_id bigint NOT NULL;

ALTER TABLE note
    ALTER COLUMN user_id uniqueidentifier NOT NULL;
Go
-- TODO constraints to user-table
