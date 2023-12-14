package org.burningokr.dto.settings;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSettingsDto {
  private Long id;
  private Long defaultCompanyId;
  private Long defaultTeamId;
}
