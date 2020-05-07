ALTER TABLE public.company_structure
    RENAME TO structure;

CREATE TABLE public.sub_structure
(
    id bigint,
    parent_structure_id bigint,
    is_active boolean,
    CONSTRAINT "PK_subStructure" PRIMARY KEY (id),
    CONSTRAINT "FK_subStructure_structure" FOREIGN KEY (id) REFERENCES public.structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT "FK_subStructure_structure_parentStructure" FOREIGN KEY (parent_structure_id) REFERENCES public.structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE public.department
    DROP COLUMN parent_structure_id;
ALTER TABLE public.department
    DROP COLUMN is_active;
-- ALTER TABLE public.department
--     DROP CONSTRAINT fkbqdg50hket4gx9j9xixvew31o;
ALTER TABLE public.department
    DROP CONSTRAINT fktqv08tc4aufiv4bu0kwcj185q;
ALTER TABLE public.department
    ADD CONSTRAINT "FK_department_structure" FOREIGN KEY (id) REFERENCES public.sub_structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE public.corporate_objective_structure
    DROP COLUMN name;
ALTER TABLE public.corporate_objective_structure
    DROP COLUMN label;
ALTER TABLE public.corporate_objective_structure
    DROP COLUMN department_structure_id;
-- ALTER TABLE public.corporate_objective_structure
--     DROP CONSTRAINT department_structure_fkey;
ALTER TABLE public.corporate_objective_structure
    DROP CONSTRAINT parent_structure_fkey;
ALTER TABLE public.corporate_objective_structure
    ADD CONSTRAINT "FK_corporateObjectiveStructure_subStructure" FOREIGN KEY (id) REFERENCES public.sub_structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE public.corporate_objective_structure
    ADD CONSTRAINT "FK_corporateObjectiveStructure_structure" FOREIGN KEY (parent_structure_id) REFERENCES public.structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
