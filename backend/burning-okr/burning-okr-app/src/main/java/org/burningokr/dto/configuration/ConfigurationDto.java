package org.burningokr.dto.configuration;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ConfigurationDto {

  private Long id;

  @NotNull
  @Size(max = 255)
  private String name;

  @NotNull
  @Size(max = 255)
  private String value;

  private String type;
}
