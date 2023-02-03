package org.burningokr.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class FeedbackDto {

  @NotNull
  private String name;

  @Size(min = 1)
  private String feedbackText;
}
