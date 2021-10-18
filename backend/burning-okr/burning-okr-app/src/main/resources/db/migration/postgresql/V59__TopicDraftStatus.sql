
ALTER TABLE okr_topic_draft ADD COLUMN current_status varchar NOT NULL default 'submitted' check (current_status in ('submitted', 'draft', 'rejected', 'approved'));

UPDATE public.okr_topic_draft SET current_status = 'submitted' where current_status IS NULL;