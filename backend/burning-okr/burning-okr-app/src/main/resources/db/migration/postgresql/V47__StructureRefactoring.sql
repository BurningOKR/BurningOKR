ALTER TABLE public.company_structure
    RENAME TO structure;

CREATE TABLE public.sub_structure
(
    id bigint,
    parent_structure_id bigint,
    is_active boolean,
    CONSTRAINT "pk_subStructure" PRIMARY KEY (id),
    CONSTRAINT "fk_subStructure_structure" FOREIGN KEY (id) REFERENCES public.structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT "fk_subStructure_structure_parentStructure" FOREIGN KEY (parent_structure_id) REFERENCES public.structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO public.sub_structure (id, parent_structure_id, is_active) SELECT id, parent_structure_id, is_active FROM public.department;

ALTER TABLE public.department
    DROP COLUMN parent_structure_id;
ALTER TABLE public.department
    DROP COLUMN is_active;
ALTER TABLE public.department
    DROP CONSTRAINT fktqv08tc4aufiv4bu0kwcj185q;
ALTER TABLE public.department
    ADD CONSTRAINT "fk_department_structure" FOREIGN KEY (id) REFERENCES public.sub_structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- It is okay to drop columns here, because the table was not used before.
ALTER TABLE public.corporate_objective_structure
    DROP COLUMN name;
ALTER TABLE public.corporate_objective_structure
    DROP COLUMN label;
ALTER TABLE public.corporate_objective_structure
    DROP COLUMN department_structure_id;
ALTER TABLE public.corporate_objective_structure
    DROP CONSTRAINT parent_structure_fkey;
ALTER TABLE public.corporate_objective_structure
    ADD CONSTRAINT "fk_corporateobjectivestructure_substructure" FOREIGN KEY (id) REFERENCES public.sub_structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE public.corporate_objective_structure
    ADD CONSTRAINT "fk_corporateobjectivestructure_structure" FOREIGN KEY (parent_structure_id) REFERENCES public.structure (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
