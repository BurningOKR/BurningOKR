CREATE TABLE dashboard_creation
(
  id         bigint NOT NULL,
  title      varchar(255),
  creator_id uniqueidentifier,
  company_id bigint,
  CONSTRAINT pk_dashboardcreation PRIMARY KEY (id)
);

CREATE TABLE chart_creation_options
(
  id                    bigint NOT NULL,
  title                 varchar(255),
  chart_type            int,
  dashboard_creation_id bigint,
  CONSTRAINT pk_chartcreationoptions PRIMARY KEY (id)
);


ALTER TABLE chart_creation_options
  ADD CONSTRAINT FK_CHARTCREATIONOPTIONS_ON_DASHBOARDCREATION FOREIGN KEY (dashboard_creation_id) REFERENCES dashboard_creation (id);

CREATE TABLE chart_creation_team
(
  chart_creation_options_id bigint,
  chart_creation_team_id bigint,
  CONSTRAINT pk_chartcreationteam PRIMARY KEY(chart_creation_options_id, chart_creation_team_id)
);

CREATE TABLE key_result_history
(
  id            bigint NOT NULL,
  start_value   bigint,
  current_value bigint,
  target_value  bigint,
  date_changed  date,
  key_result_id bigint,
  CONSTRAINT pk_keyresulthistory PRIMARY KEY (id)
);

ALTER TABLE key_result_history
  ADD CONSTRAINT FK_KEYRESULTHISTORY_ON_KEYRESULT FOREIGN KEY (key_result_id) REFERENCES key_result (id);

INSERT INTO key_result_history (id, start_value, current_value, target_value, date_changed, key_result_id)
SELECT (NEXT VALUE FOR hibernate_sequence), start_value, current_value, target_value, CAST(GETDATE() AS Date), id FROM key_result;
