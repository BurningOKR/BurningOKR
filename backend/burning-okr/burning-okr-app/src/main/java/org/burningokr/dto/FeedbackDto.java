package org.burningokr.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackDto {

  @NotNull
  private String name;

  @Size(min = 1)
  private String feedbackText;
}
