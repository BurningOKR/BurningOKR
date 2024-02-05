package org.burningokr.model.dashboard.creation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.Collection;
import java.util.UUID;

@Data
public class DashboardCreationDto {
  private Long id;

  @Size(min = 1, max = 255, message = "The title of the Dashboard may not be longer than 255 characters")
  private String title;

  @NotNull
  private UUID creatorId;

  @NotNull
  private Long companyId;
  private Collection<ChartCreationOptionsDto> chartCreationOptions;
}
