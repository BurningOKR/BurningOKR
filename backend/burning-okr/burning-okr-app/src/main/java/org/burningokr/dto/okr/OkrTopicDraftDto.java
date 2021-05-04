package org.burningokr.dto.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.users.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class OkrTopicDraftDto extends OkrTopicDescriptionDto {
  private Long okrParentUnitId;
  private User initiator;
  private OkrTopicDraftStatusEnum currentStatus;
}
