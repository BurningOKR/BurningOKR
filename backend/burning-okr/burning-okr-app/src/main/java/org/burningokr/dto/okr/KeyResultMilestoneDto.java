package org.burningokr.dto.okr;

import lombok.Data;

@Data
public class KeyResultMilestoneDto {

  private Long id;

  private Long parentKeyResultId;

  private String name;

  private Long value;
}
