-- INSERT INTO public.okr_topic_description (id, name)
-- SELECT nextval('hibernate_sequence') as id, u.name from okr_department d
-- INNER JOIN okr_unit u ON d.id = u.id
-- WHERE d.okr_topic_sponsor_id IS NULL;

-- Start a new transaction
BEGIN;

CREATE TEMPORARY TABLE temp_ids (
    okr_department_id bigint,
    okr_description_id bigint
);

INSERT INTO temp_ids (okr_department_id, okr_description_id)
SELECT nextval('hibernate_sequence') as id, u.name from okr_department d
INNER JOIN okr_unit u ON d.id = u.id
WHERE d.okr_topic_sponsor_id IS NULL;

-- First insert new ids for the
UPDATE public.okr_department
SET okr_topic_description_id = nextval('hibernate_sequence')
WHERE okr_topic_description_id IS NULL;

-- Insert OKR Topic Descriptions for all ids that do not exist yet
INSERT INTO public.okr_topic_description (id, name)
SELECT id, name FROM public.okr_department d
INNER JOIN public.okr_unit u ON d.id = u.id
WHERE d.okr_topic_description_id NOT IN
(
    SELECT id FROM public.okr_topic_description
);

-- Commit the transaction
COMMIT;

