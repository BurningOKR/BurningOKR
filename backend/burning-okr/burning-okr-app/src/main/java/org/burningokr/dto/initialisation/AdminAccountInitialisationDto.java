package org.burningokr.dto.initialisation;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.burningokr.dto.users.LocalUserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountInitialisationDto {

  @NotNull private LocalUserDto userDto;

  @NotNull private String password;
}
