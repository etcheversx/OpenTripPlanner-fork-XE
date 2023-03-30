package org.opentripplanner.openstreetmap.model;

import java.util.NoSuchElementException;

public class OptionalBoolean {
  private final static OptionalBoolean empty = new OptionalBoolean();
  private final static OptionalBoolean yes = new OptionalBoolean("true");
  private final static OptionalBoolean no = new OptionalBoolean("false");


  private OptionalBoolean(String s) {
  }

  private OptionalBoolean() {
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

  public boolean isPresent() {
    return (! this.isEmpty());
  }

  public boolean getAsBoolean() {
    if (!isPresent()) {
      throw new NoSuchElementException("No value present");
    }
    return this.equals(yes);
  }

  public boolean isEmpty() {
    return this.equals(empty);
  }
}
