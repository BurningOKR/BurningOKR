package org.burningokr.dto.users;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  private UUID id;
//  @NotNull(message = "The field given_name should not be null")
  private String givenName;
//  @NotNull
  private String surname;
  private String mail;
  private String jobTitle;
  private String department;
  private String photo;
  private boolean active;
  private boolean admin;
}
