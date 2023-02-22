package org.burningokr.dto.initialisation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.burningokr.dto.users.UserDto;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountInitialisationDto {

  @NotNull
  private UserDto userDto;

  @NotNull
  private String password;
}
