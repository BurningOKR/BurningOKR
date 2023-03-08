package org.burningokr.dto.users;

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
  private String givenName;
  private String surname;
  private String mail;
  private String jobTitle;
  private String department;
  private String photo;
  private boolean active;
}
