ALTER TABLE public.okr_topic_draft DROP COLUMN IF EXISTS status;

DROP TYPE status;
CREATE TYPE status as ENUM ('draft', 'submitted', 'approved', 'rejected');

ALTER TABLE okr_topic_draft ADD COLUMN current_status status NOT NULL default 'submitted';

UPDATE public.okr_topic_draft SET current_status = 'submitted' where current_status IS NULL;