CREATE TABLE public.okr_topic_description
(
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    initiator_id uuid,
    acceptance_criteria character varying(1023),
    contributes_to character varying(1023),
    delimitation character varying(1023),
    dependencies character varying(1023),
    resources character varying(1023),
    handover_plan character varying(1023),
    beginning date,
    CONSTRAINT pk_okr_topic_description PRIMARY KEY (id)
);

CREATE TABLE public.okr_description_member
(
    okr_topic_description_id bigint NOT NULL,
    okr_member_id uuid,
    CONSTRAINT okr_member_description_fkey FOREIGN KEY (okr_topic_description_id)
        REFERENCES public.okr_topic_description (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.okr_description_stakeholder
(
    okr_topic_description_id bigint NOT NULL,
    okr_stakeholder_id uuid,
    CONSTRAINT okr_stakeholder_description_fkey FOREIGN KEY (okr_topic_description_id)
        REFERENCES public.okr_topic_description (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE public.okr_department ADD okr_topic_description_id bigint;
ALTER TABLE public.okr_department ADD CONSTRAINT okr_topic_description_fkey FOREIGN KEY (okr_topic_description_id)
    REFERENCES public.okr_topic_description (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION;
