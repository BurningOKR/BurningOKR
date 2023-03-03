package org.burningokr.dto.initialisation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.burningokr.dto.users.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountInitialisationDto {

  @NotNull
  private UserDto userDto;

  @NotNull
  private String password;
}
