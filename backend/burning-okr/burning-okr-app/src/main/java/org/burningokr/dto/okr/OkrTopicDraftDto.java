package org.burningokr.dto.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OkrTopicDraftDto extends OkrTopicDescriptionDto{
    private Long okrParentUnitId;
}
