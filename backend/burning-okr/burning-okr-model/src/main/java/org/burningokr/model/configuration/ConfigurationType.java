package org.burningokr.model.configuration;

public enum ConfigurationType {
  TEXT("text"),
  NUMBER("number"),
  CHECKBOX("checkbox");

  private String name;

  ConfigurationType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
