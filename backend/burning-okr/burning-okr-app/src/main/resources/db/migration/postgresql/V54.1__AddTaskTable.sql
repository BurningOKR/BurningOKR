CREATE TABLE public.task_board
(
    id bigint NOT NULL,
    CONSTRAINT task_board_pkey primary key (id),
    parent_unit_id bigint NOT NULL,
    CONSTRAINT task_board_parent_unit_fkey FOREIGN KEY (parent_unit_id)
        REFERENCES public.okr_department (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.task_state
(
    id bigint NOT NULL,
    CONSTRAINT task_state_pkey PRIMARY KEY (id),

    title character varying(255) NOT NULL,
    parent_task_board_id bigint NOT NULL,
    CONSTRAINT task_state_task_board_fkey FOREIGN KEY (parent_task_board_id)
        REFERENCES public.task_board (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.task
(
   id bigint NOT NULL,
   CONSTRAINT task_pkey PRIMARY KEY (id),

   title character varying(255) Not Null,
   description character varying(255),
   version bigint,
   task_state_id bigint,
   CONSTRAINT task_task_state_fkey FOREIGN key (task_state_id)
        REFERENCES public.task_state(id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

   assigned_key_result_id bigint,
   CONSTRAINT task_assigned_key_result_fkey FOREIGN key (assigned_key_result_id)
        REFERENCES public.key_result (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

   parent_task_board_id bigint NOT NULL,
   CONSTRAINT task_parent_task_board_fkey FOREIGN KEY (parent_task_board_id)
        REFERENCES public.task_board(id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

   previous_task_id bigint,
   CONSTRAINT task_previous_task_id FOREIGN KEY (previous_task_id)
        REFERENCES public.task(id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.task_user
(
    task_id bigint NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT task_fkey FOREIGN KEY (task_id)
        REFERENCES public.task (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE TABLE public.default_task_board_state
(
    id bigint NOT NULL,
    CONSTRAINT default_task_board_state_pkey PRIMARY KEY (id),
    title character varying(255) NOT NULL
);

INSERT INTO public.task_board (id, parent_unit_id)
SELECT department.id,department.id FROM okr_department department;

Insert INTO public.default_task_board_state(id,title) values
(1,'ToDo'),(2,'In Progress'),(3, 'Blocked'),(4, 'Finished');



INSERT INTO public.task_state (id,title,parent_task_board_id)
SELECT taskboard.id +defaultstates.id , defaultstates.title ,taskboard.id
FROM public.task_board taskboard, public.default_task_board_state defaultstates;
