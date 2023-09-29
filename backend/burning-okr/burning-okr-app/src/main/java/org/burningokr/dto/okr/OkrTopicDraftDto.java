package org.burningokr.dto.okr;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.users.User;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class OkrTopicDraftDto extends OkrTopicDescriptionDto {
  private Long okrParentUnitId;
  private User initiator;

  @PositiveOrZero
  private int currentStatus;
}
