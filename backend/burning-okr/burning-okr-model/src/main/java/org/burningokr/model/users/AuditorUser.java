package org.burningokr.model.users;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class AuditorUser {
  @Id
  private UUID id;
}
