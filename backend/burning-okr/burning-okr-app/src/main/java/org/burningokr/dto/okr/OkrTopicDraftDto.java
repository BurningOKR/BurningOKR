package org.burningokr.dto.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.users.IUser;

@Data
@EqualsAndHashCode(callSuper = true)
public class OkrTopicDraftDto extends OkrTopicDescriptionDto {
  private Long okrParentUnitId;
  private IUser initiator;
  private int currentStatus;
}
