package org.burningokr.dto.okr;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TaskDto {
  private Long id;

  @NotNull
  @Size(
    min = 1,
    max = 255,
    message = "The title of an objective may not be empty or longer than {max} characters.")
  private String title;

  @Size(
    max = 1023,
    message = "The description of an objective may not be longer than {max} characters."
  )
  private String description;

  @NotNull
  private Long taskStateId;

  private Collection<UUID> assignedUserIds = new ArrayList<>();
  private Long assignedKeyResultId;
  @NotNull
  private Long parentTaskBoardId;
  private Long previousTaskId;
  private Long version;

  public boolean hasAssignedKeyResult() {
    return assignedKeyResultId != null;
  }
}
