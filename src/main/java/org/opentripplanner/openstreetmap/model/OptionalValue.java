package org.opentripplanner.openstreetmap.model;

public interface OptionalValue<T> {
  boolean isEmpty();

  boolean isPresent();

  T getAsTyped();
}
