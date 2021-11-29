package org.burningokr.service.environment;

public enum AuthModes {
  AZURE("azure"),
  LOCAL("local"),
  DEMO("demo");

  private String name;

  AuthModes(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
