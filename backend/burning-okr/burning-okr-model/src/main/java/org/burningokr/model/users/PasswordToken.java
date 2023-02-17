package org.burningokr.model.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class PasswordToken {
  @Id
  UUID emailIdentifier;
  UUID localUserId;
  LocalDateTime createdAt;
}
