-- Add new column
ALTER TABLE dbo.task_state ADD id_temp bigint;
GO

BEGIN TRANSACTION
-- Drop PKEY/FKEY Constraints
ALTER TABLE dbo.task DROP CONSTRAINT task_task_state_fkey;
ALTER TABLE dbo.task_state DROP CONSTRAINT task_state_pkey;



-- Copy Data
UPDATE dbo.task_state SET id_temp = id;


-- Drop old column
ALTER TABLE dbo.task_state DROP COLUMN id;


-- Rename new column to old column
EXEC sp_rename 'task_state.id_temp', 'id', 'COLUMN';


-- Re-Add Constraints
ALTER TABLE dbo.task_state ALTER COLUMN id bigint NOT NULL
ALTER TABLE dbo.task_state ADD CONSTRAINT task_state_pkey PRIMARY KEY (id);

ALTER TABLE dbo.task ADD CONSTRAINT task_task_state_fkey FOREIGN key (task_state_id)
    REFERENCES task_state(id)
    ON UPDATE NO ACTION ON DELETE NO ACTION;
COMMIT
