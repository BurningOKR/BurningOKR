ALTER TABLE okr_company_history RENAME TO okr_unit_history;

ALTER TABLE okr_department ADD COLUMN history_id bigint;
ALTER TABLE okr_branch ADD COLUMN history_id bigint;

ALTER TABLE okr_department ADD CONSTRAINT fk_okr_department_history FOREIGN KEY (history_id)
    REFERENCES public.okr_unit_history (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_branch ADD CONSTRAINT fk_okr_branch_history FOREIGN KEY (history_id)
    REFERENCES public.okr_unit_history (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;
