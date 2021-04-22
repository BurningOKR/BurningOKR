CREATE TYPE status as ENUM ('Entwurf', 'Eingereicht', 'Genehmigt', 'Abgelehnt');

ALTER TABLE okr_topic_draft ADD COLUMN status status NOT NULL default 'Eingereicht';

UPDATE public.okr_topic_draft SET status = 'Eingereicht' where status IS NULL;