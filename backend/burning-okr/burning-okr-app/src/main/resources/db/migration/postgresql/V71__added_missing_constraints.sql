-- General missing constraints
ALTER TABLE dashboard_creation
    ALTER COLUMN company_id SET NOT NULL;

ALTER TABLE dashboard_creation
    ALTER COLUMN creator_id SET NOT NULL;

ALTER TABLE key_result_history
    ALTER COLUMN current_value SET NOT NULL;

ALTER TABLE objective
    ALTER COLUMN is_active SET NOT NULL;

ALTER TABLE cycle
    ALTER COLUMN is_visible SET NOT NULL;

ALTER TABLE key_result_history
    ALTER COLUMN key_result_id SET NOT NULL;

ALTER TABLE key_result
    ALTER COLUMN sequence SET NOT NULL;

ALTER TABLE objective
    ALTER COLUMN sequence SET NOT NULL;

ALTER TABLE key_result_history
    ALTER COLUMN start_value SET NOT NULL;

ALTER TABLE key_result_history
    ALTER COLUMN target_value SET NOT NULL;

ALTER TABLE task
    ALTER COLUMN task_state_id SET NOT NULL;

ALTER TABLE note
    ALTER COLUMN user_id SET NOT NULL;

-- TODO constraints to user-table
