package org.burningokr.dto.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrontendLogDto {

  private Long id;
  @NotNull
  private String level;
  @NotNull
  private String timestamp;
  @NotNull
  private String fileName;
  @NotNull
  private String lineNumber;
  @NotNull
  private String message;
}
