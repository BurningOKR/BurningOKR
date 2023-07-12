package org.burningokr.model.monitoring;

import lombok.Getter;
import lombok.ToString;
import org.burningokr.model.users.User;
import org.springframework.security.core.parameters.P;

import java.util.Objects;
import java.util.UUID;

@ToString
public class UserId {

  @Getter
  private final UUID userId;

  public UserId(User user) {
    this.userId = user.getId();
  }

  public UserId(String userId) {
    this.userId = UUID.fromString(userId);
  }

  public UserId(UUID userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserId that = (UserId) o;
    return Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }
}
