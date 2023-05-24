package org.opentripplanner.openstreetmap.model;

import java.util.NoSuchElementException;
import java.util.Objects;

public class OptionalBoolean implements OptionalValue {

  private static final OptionalBoolean empty = new OptionalBoolean();
  private static final OptionalBoolean yes = new OptionalBoolean("true");
  private static final OptionalBoolean no = new OptionalBoolean("false");
  private final Boolean value;

  private OptionalBoolean(String s) {
    this.value = Boolean.parseBoolean(s);
  }

  private OptionalBoolean() {
    this.value = null;
  }

  public static OptionalBoolean empty() {
    return empty;
  }

  public static OptionalBoolean of(boolean value) {
    if (value) {
      return yes;
    } else {
      return no;
    }
  }

  public static OptionalBoolean yes() {
    return yes;
  }

  public static OptionalBoolean no() {
    return no;
  }

  @Override
  public boolean isPresent() {
    return (!this.isEmpty());
  }

  @Override
  public Boolean getAsTyped() {
    if (!isPresent()) {
      throw new NoSuchElementException("No value present");
    }
    return this.equals(yes);
  }

  @Override
  public boolean isEmpty() {
    return this.equals(empty);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OptionalBoolean that = (OptionalBoolean) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
