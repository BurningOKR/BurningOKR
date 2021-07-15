ALTER TABLE public.note
    ADD COLUMN parent_object_type varchar NOT NULL default 'keyResult' check (parent_object_type in ('keyResult', 'topicDraft'));

ALTER TABLE public.note
    RENAME COLUMN parent_key_result_id TO parent_object_id;
