package org.opentripplanner.openstreetmap.model;

public interface OptionalValue {
  boolean isEmpty();

  boolean isPresent();

  Object getAsTyped();
}
