package org.burningokr.dto.settings;

import lombok.Data;

@Data
public class UserSettingsDto {
  private Long id;
  private Long defaultCompanyId;
  private Long defaultTeamId;
}
