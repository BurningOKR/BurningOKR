CREATE TABLE task_board
(
    id bigint NOT NULL,
    parent_unit_id bigint NOT NULL,
    CONSTRAINT task_board_pkey primary key (id),
    CONSTRAINT task_board_parent_unit_fkey FOREIGN KEY (parent_unit_id)
        REFERENCES okr_department (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE task_state
(
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    parent_task_board_id bigint NOT NULL,
    CONSTRAINT task_state_pkey PRIMARY KEY (id),
    CONSTRAINT task_state_task_board_fkey FOREIGN KEY (parent_task_board_id)
        REFERENCES task_board (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE task
(
   id bigint NOT NULL,
   title character varying(255) Not Null,
   description character varying(255),
   version bigint,
   task_state_id bigint,
   assigned_key_result_id bigint,
   parent_task_board_id bigint NOT NULL,
   previous_task_id bigint,
   CONSTRAINT task_pkey PRIMARY KEY (id),
   CONSTRAINT task_task_state_fkey FOREIGN key (task_state_id)
        REFERENCES task_state(id)
        ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT task_assigned_key_result_fkey FOREIGN key (assigned_key_result_id)
        REFERENCES key_result (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT task_parent_task_board_fkey FOREIGN KEY (parent_task_board_id)
        REFERENCES task_board(id)
        ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT task_previous_task_id FOREIGN KEY (previous_task_id)
        REFERENCES task(id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE task_user
(
    task_id bigint NOT NULL,
    user_id uniqueidentifier NOT NULL,
    CONSTRAINT task_fkey FOREIGN KEY (task_id)
        REFERENCES task (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE TABLE default_task_board_state
(
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    CONSTRAINT default_task_board_state_pkey PRIMARY KEY (id)
);

INSERT INTO task_board (id, parent_unit_id)
SELECT department.id,department.id FROM okr_department department;

Insert INTO default_task_board_state(id,title) values
(1,'ToDo'),(2,'In Progress'),(3, 'Blocked'),(4, 'Finished');



INSERT INTO task_state (id,title,parent_task_board_id)
SELECT taskboard.id +defaultstates.id , defaultstates.title ,taskboard.id
FROM task_board taskboard, default_task_board_state defaultstates;
