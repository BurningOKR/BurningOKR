package org.burningokr.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackDto {

  @NotNull private String name;

  @Size(min = 1)
  private String feedbackText;
}
