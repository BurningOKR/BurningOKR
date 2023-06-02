ALTER TABLE okr_unit DROP COLUMN history_id;

ALTER TABLE okr_unit ADD COLUMN company_history_id BIGINT,
                     ADD CONSTRAINT fk_company_history FOREIGN KEY (company_history_id) REFERENCES okr_unit_history (id) ON DELETE CASCADE;

ALTER TABLE okr_unit ADD COLUMN branch_history_id BIGINT,
                     ADD CONSTRAINT fk_branch_history FOREIGN KEY (branch_history_id) REFERENCES okr_unit_history (id) ON DELETE CASCADE;

ALTER TABLE okr_unit ADD COLUMN department_history_id BIGINT,
                     ADD CONSTRAINT fk_department_history FOREIGN KEY (department_history_id) REFERENCES okr_unit_history (id) ON DELETE CASCADE;
