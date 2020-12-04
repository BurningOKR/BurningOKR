ALTER TABLE aad_user
    ALTER COLUMN id TYPE uuid USING (id::uuid);

ALTER TABLE department
    ALTER COLUMN okr_master_id TYPE uuid  USING (okr_master_id::uuid),
    ALTER COLUMN okr_topic_sponsor_id TYPE uuid USING (okr_topic_sponsor_id::uuid);

ALTER TABLE department_okr_member_ids
  RENAME TO okr_member;

ALTER TABLE okr_member
    RENAME okr_member_ids TO okr_member_id;

ALTER TABLE okr_member
    ALTER COLUMN okr_member_id TYPE uuid USING (okr_member_id::uuid);

ALTER TABLE note
    ALTER COLUMN user_id TYPE uuid USING (user_id::uuid);

ALTER TABLE user_settings
    ALTER COLUMN user_id TYPE uuid USING (user_id::uuid);
