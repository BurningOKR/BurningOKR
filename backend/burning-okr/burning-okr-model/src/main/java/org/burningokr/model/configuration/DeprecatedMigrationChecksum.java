package org.burningokr.model.configuration;

import lombok.Data;

@Data
public class DeprecatedMigrationChecksum {
  private String version;
  private int oldChecksum;
}
