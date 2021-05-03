package org.burningokr.dto.okr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskDto {
  private Long id;

  @Size(max = 255, message = "The title of an objective may not be longer than 255 characters.")
  private String title;

  @Size(
      max = 1023,
      message = "The description of an objective is not allowed to be longer than 1023 characters.")
  private String description;

  @NotNull private Long taskStateId;

  private Collection<UUID> assignedUserIds = new ArrayList<>();

  public boolean hasAssignedUsers() {
    return assignedUserIds.isEmpty();
  }

  private Long assignedKeyResultId;

  public boolean hasAssignedKeyResult() {
    return assignedKeyResultId != null;
  }

  @NotNull private Long parentTaskBoardId;

  private Long previousTaskId;

  private Long version;
}
