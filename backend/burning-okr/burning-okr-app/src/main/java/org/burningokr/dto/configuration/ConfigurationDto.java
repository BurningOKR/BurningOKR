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
  @Size(max = 255, message = "The name of the Configuration may not be longer than 255 characters")
  private String name;

  @NotNull
  @Size(max = 255, message = "The value of the Configuration may not be longer than 255 characters")
  private String value;

  private String type;
}
