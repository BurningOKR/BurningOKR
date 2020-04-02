package org.burningokr.model.okr;

public enum Unit {
  NUMBER(""),
  PERCENT("%"),
  EURO("€");

  private final String value;

  Unit(final String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
