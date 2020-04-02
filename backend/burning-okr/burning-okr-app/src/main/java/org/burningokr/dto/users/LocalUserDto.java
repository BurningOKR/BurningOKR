package org.burningokr.dto.users;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class LocalUserDto {
  private UUID id;
  private String givenName;
  private String surname;
  private String email;
  private String jobTitle;
  private String department;
  private String photo;
  private boolean active;
  private LocalDateTime createdAt;
}
