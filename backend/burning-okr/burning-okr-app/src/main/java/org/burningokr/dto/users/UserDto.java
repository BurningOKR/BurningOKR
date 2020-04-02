package org.burningokr.dto.users;

import java.util.UUID;
import lombok.Data;

@Data
public class UserDto {

  private UUID id;
  private String givenName;
  private String surname;
  private String email;
  private String jobTitle;
  private String department;
  private String photo;
}
