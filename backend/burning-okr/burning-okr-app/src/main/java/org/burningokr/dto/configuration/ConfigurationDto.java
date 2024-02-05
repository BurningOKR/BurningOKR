package org.burningokr.dto.configuration;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDto {

  private Long id;

  @NotNull
  @Size(
    min = 1,
    max = 255,
    message = "The name of the configuration may not be empty or longer than {max} characters"
  )
  private String name;

  @NotNull
  @Size(
    min = 1,
    max = 255,
    message = "The value of the configuration may not be empty or longer than {max} characters"
  )
  private String value;

  private String type;
}
