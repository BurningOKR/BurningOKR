package org.burningokr.service.security;

public enum UserContextRole {
  // Keep the order of these context roles in order please
  ADMIN(0),
  OKRMANAGER(1),
  OKRMEMBER(2),
  ENTITYOWNER(3),
  USER(4);

  private final int ranking;

  UserContextRole(int ranking) {
    this.ranking = ranking;
  }

  boolean isLowerAuthorityTypeThan(UserContextRole otherActivityType) {
    return (this.ranking > otherActivityType.ranking);
  }

  boolean isHigherAuthorityTypeThan(UserContextRole otherActivityType) {
    return (this.ranking < otherActivityType.ranking);
  }
}
