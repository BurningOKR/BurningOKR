ALTER TABLE public.structure
    RENAME TO okr_unit;
ALTER TABLE public.company
    RENAME TO okr_company;
ALTER TABLE public.company_history
    RENAME TO okr_company_history;
ALTER TABLE public.corporate_objective_structure
    RENAME TO okr_branch;
ALTER TABLE public.department
    RENAME TO okr_department;
ALTER TABLE public.sub_structure
    RENAME TO okr_child_unit;

ALTER TABLE public.objective
    RENAME COLUMN parent_structure_id TO parent_okr_unit_id;
ALTER TABLE public.okr_child_unit
    RENAME COLUMN parent_structure_id TO parent_okr_unit_id;
ALTER TABLE public.okr_branch
    RENAME COLUMN parent_structure_id TO parent_okr_unit_id;

ALTER TABLE public.user_settings
    RENAME COLUMN default_company_id TO default_okr_company_id;
ALTER TABLE public.okr_member
    RENAME COLUMN department_id TO okr_department_id;