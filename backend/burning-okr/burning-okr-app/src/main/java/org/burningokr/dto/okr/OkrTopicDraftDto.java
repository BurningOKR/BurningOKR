package org.burningokr.dto.okr;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.burningokr.model.users.User;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OkrTopicDraftDto extends OkrTopicDescriptionDto {
  private Long okrParentUnitId;
  private User initiator;

  @PositiveOrZero
  private int currentStatus;


  public OkrTopicDraftDto(OkrTopicDescriptionDto parentUnit) {
    super(parentUnit);
  }
}
