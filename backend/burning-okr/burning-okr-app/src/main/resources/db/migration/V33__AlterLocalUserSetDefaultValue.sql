ALTER TABLE local_user
    ALTER COLUMN mail SET NOT NULL;

ALTER TABLE local_user
    ALTER COLUMN surname SET NOT NULL;

ALTER TABLE local_user
    ALTER COLUMN given_name SET NOT NULL;

ALTER TABLE local_user
    ALTER COLUMN created_at SET DEFAULT NOW();

ALTER TABLE local_user
    ALTER COLUMN created_at SET NOT NULL;

