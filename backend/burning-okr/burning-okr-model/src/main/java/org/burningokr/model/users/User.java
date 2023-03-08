package org.burningokr.model.users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class User implements IUser {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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
