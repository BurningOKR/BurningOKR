ALTER TABLE okr_unit
    ADD COLUMN company_history_id BIGINT,
    ADD CONSTRAINT fk_company_history FOREIGN KEY (company_history_id) REFERENCES okr_unit_history (id);

ALTER TABLE okr_unit
    ADD COLUMN branch_history_id BIGINT,
    ADD CONSTRAINT fk_branch_history FOREIGN KEY (branch_history_id) REFERENCES okr_unit_history (id);

ALTER TABLE okr_unit
    ADD COLUMN department_history_id BIGINT,
    ADD CONSTRAINT fk_department_history FOREIGN KEY (department_history_id) REFERENCES okr_unit_history (id);

UPDATE okr_unit SET company_history_id = history_id WHERE okr_unit_type = 'OKR_COMPANY';
UPDATE okr_unit SET department_history_id = history_id WHERE okr_unit_type = 'OKR_DEPARTMENT';
UPDATE okr_unit SET branch_history_id = history_id WHERE okr_unit_type = 'OKR_BRANCH';

ALTER TABLE okr_unit
    DROP COLUMN history_id;
