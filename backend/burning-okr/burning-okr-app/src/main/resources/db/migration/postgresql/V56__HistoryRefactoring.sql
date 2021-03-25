CREATE TABLE okr_unit_history
(
    id bigint NOT NULL,
    CONSTRAINT pk_okr_unit_history PRIMARY KEY(id)
);


CREATE TABLE okr_branch_history
(
    id bigint NOT NULL,
    CONSTRAINT pk_okr_branch_history PRIMARY KEY(id)
);

CREATE TABLE okr_department_history
(
    id bigint NOT NULL,
    CONSTRAINT pk_okr_department_history PRIMARY KEY(id)
);

ALTER TABLE okr_department ADD COLUMN history_id bigint;
ALTER TABLE okr_branch ADD COLUMN history_id bigint;


INSERT INTO okr_unit_history(id) (SELECT id FROM public.okr_company_history);

UPDATE public.okr_department SET history_id = nextval('hibernate_sequence');
UPDATE public.okr_branch SET history_id = nextval('hibernate_sequence');

INSERT INTO public.okr_department_history(id) (SELECT history_id FROM public.okr_department);
INSERT INTO public.okr_branch_history(id) (SELECT history_id FROM public.okr_branch);

INSERT INTO public.okr_unit_history(id) (SELECT id FROM public.okr_department_history);
INSERT INTO public.okr_unit_history(id) (SELECT id FROM public.okr_branch_history);



ALTER TABLE okr_company_history ADD CONSTRAINT fk_okr_unit_history FOREIGN KEY (id)
    REFERENCES public.okr_unit_history (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_department ADD CONSTRAINT fk_okr_department_history FOREIGN KEY (history_id)
   REFERENCES public.okr_department_history (id) MATCH SIMPLE
   ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_department_history ADD CONSTRAINT fk_okr_unit_history FOREIGN KEY (id)
    REFERENCES public.okr_unit_history (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_branch ADD CONSTRAINT fk_okr_branch_history FOREIGN KEY (history_id)
    REFERENCES public.okr_branch_history (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE okr_branch_history ADD CONSTRAINT fk_okr_unit_history FOREIGN KEY (id)
    REFERENCES public.okr_unit_history (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;