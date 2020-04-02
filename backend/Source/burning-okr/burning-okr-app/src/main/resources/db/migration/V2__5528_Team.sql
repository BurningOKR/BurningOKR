-- Table: public.department_okr_member_ids

-- DROP TABLE public.department_okr_member_ids;

CREATE TABLE public.department_okr_member_ids
(
    department_id bigint NOT NULL,
    okr_member_ids character varying(255),
    CONSTRAINT fk5c2gx62q2posf5ev6d8ctmh71 FOREIGN KEY (department_id)
        REFERENCES public.department (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
    WITH (
        OIDS=FALSE
    );
ALTER TABLE public.department_okr_member_ids
    OWNER TO admin;


ALTER TABLE public.department ADD okr_master_id character varying(255);
ALTER TABLE public.department ADD okr_topic_sponsor_id character varying(255);