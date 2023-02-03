package org.burningokr.model.users;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
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
