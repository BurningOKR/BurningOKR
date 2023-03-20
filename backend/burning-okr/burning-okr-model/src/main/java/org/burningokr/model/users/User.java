package org.burningokr.model.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements IUser {
  @Id
  private UUID id;
  private String givenName;
  private String surname;
  private String mail;
  private String jobTitle;
  private String department;
  private String photo; // Represents the image as String
  private boolean active;
  private LocalDateTime createdAt;
}
