package org.burningokr.model.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class AuditorUser {
  @Id
  private UUID id;
}
