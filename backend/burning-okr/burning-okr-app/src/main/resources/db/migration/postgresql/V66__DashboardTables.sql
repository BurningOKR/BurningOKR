CREATE TABLE dashboard_creation
(
  id         BIGINT NOT NULL,
  title      VARCHAR(255),
  creator_id UUID,
  company_id BIGINT,
  CONSTRAINT pk_dashboardcreation PRIMARY KEY (id)
);


CREATE TABLE chart_creation_options
(
  id                    BIGINT NOT NULL,
  title                 VARCHAR(255),
  chart_type            INTEGER,
  dashboard_creation_id BIGINT,
  CONSTRAINT pk_chartcreationoptions PRIMARY KEY (id)
);

ALTER TABLE chart_creation_options
  ADD CONSTRAINT FK_CHARTCREATIONOPTIONS_ON_DASHBOARDCREATION FOREIGN KEY (dashboard_creation_id) REFERENCES dashboard_creation (id);
