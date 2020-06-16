-- It is okay to drop this column, because the okr_child_unit table also has this column
-- And okr_branch inherits from okr_child_unit
ALTER TABLE public.okr_branch
    DROP COLUMN parent_okr_unit_id;

INSERT INTO public.okr_branch (id)
    SELECT department.id FROM okr_department department
    WHERE department.id IN (SELECT child_unit.parent_okr_unit_id FROM public.okr_child_unit child_unit);

-- It is okay to delete here, because we moved all departments, that are going to be deleted
-- to the okr_branch table before...
DELETE FROM public.okr_department department
    WHERE department.id IN (SELECT branch.id FROM public.okr_branch branch);
