ALTER TABLE okr_topic_draft ADD current_status varchar(20) CHECK(current_status IN('draft', 'submitted', 'approved', 'rejected')) NOT NULL default 'submitted';
GO

UPDATE okr_topic_draft SET current_status = 'submitted' where current_status IS NULL;