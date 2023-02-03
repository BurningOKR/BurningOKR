package org.burningokr.dto.initialisation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.burningokr.dto.users.LocalUserDto;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountInitialisationDto {

  @NotNull
  private LocalUserDto userDto;

  @NotNull
  private String password;
}
