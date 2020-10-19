CREATE TABLE public.key_result_milestones
(
   id bigint NOT NULL,
   name character varying(255) NOT NULL,
   value bigint NOT NULL,
   parent_key_result_id bigint,
   CONSTRAINT pk_key_result_milestones PRIMARY KEY (id),
   CONSTRAINT fk_key_result_milesones_keyresults FOREIGN KEY (parent_key_result_id) REFERENCES public.key_result (id)
);
