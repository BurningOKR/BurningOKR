package org.burningokr.service.environment;

public enum EnvironmentPropertyNames {
  AUTH_MODE("auth-mode");

  public static final String ENVIRONMENT_PREFIX = "system.configuration";
  private String name;

  EnvironmentPropertyNames(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public String getFullName() {
    return ENVIRONMENT_PREFIX + "." + this.name;
  }
}
