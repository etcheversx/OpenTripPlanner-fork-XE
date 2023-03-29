package org.opentripplanner.openstreetmap.model;

public enum OptionalBoolean {
  _true("true"),
  _false("false");

  private static final OptionalBoolean empty = null;

  OptionalBoolean(String s) {}

  public static OptionalBoolean empty() {
    return empty;
  }

  public static OptionalBoolean of(boolean value) {
    if (value) {
      return _true;
    } else {
      return _false;
    }
  }
}
