-- Start a new transaction
BEGIN;

-- Create a table to match the new description ids with the department ids
CREATE TEMPORARY TABLE temp_ids (
    okr_department_id bigint,
    okr_description_id bigint
);

-- Create a description id for every department, which does not have an okr_description_id
INSERT INTO temp_ids (okr_department_id, okr_description_id)
SELECT d.id, nextval('hibernate_sequence') as okr_description_id from okr_department d
WHERE d.okr_topic_description_id IS NULL;

-- Create an okr_topic_description for every new id in the temp table
INSERT INTO public.okr_topic_description (id, name)
SELECT temp_ids.okr_description_id, u.name FROM temp_ids
INNER JOIN okr_unit u on u.id = temp_ids.okr_department_id;

-- Insert the description id in the okr_department table
UPDATE public.okr_department od
SET okr_topic_description_id = temp_ids.okr_description_id
FROM temp_ids
WHERE od.id = temp_ids.okr_department_id;

-- Commit the transaction
COMMIT;

