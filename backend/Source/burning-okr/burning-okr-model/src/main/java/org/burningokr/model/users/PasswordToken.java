package org.burningokr.model.users;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PasswordToken {
  @Id UUID emailIdentifier;
  UUID localUserId;
  LocalDateTime createdAt;
}
