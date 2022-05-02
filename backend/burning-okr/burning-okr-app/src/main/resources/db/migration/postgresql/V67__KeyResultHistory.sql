CREATE TABLE key_result_history
(
  id            BIGINT NOT NULL,
  start_value   BIGINT,
  current_value BIGINT,
  target_value  BIGINT,
  date_changed  date,
  key_result_id BIGINT,
  CONSTRAINT pk_keyresulthistory PRIMARY KEY (id)
);

ALTER TABLE key_result_history
  ADD CONSTRAINT FK_KEYRESULTHISTORY_ON_KEYRESULT FOREIGN KEY (key_result_id) REFERENCES key_result (id);


INSERT INTO key_result_history (id, start_value, current_value, target_value, date_changed, key_result_id)
SELECT (nextval('public.hibernate_sequence')), start_value, current_value, target_value, CURRENT_DATE, id FROM key_result;
