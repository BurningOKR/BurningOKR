package org.burningokr.dto.users;

import lombok.Data;

import java.util.UUID;

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
