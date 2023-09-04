package org.burningokr.dto.settings;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSettingsDto {
  @NotNull
  private Long id;
  private Long defaultCompanyId;

  private Long defaultTeamId;
}
