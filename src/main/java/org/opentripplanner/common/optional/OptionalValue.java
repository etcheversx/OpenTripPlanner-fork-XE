package org.opentripplanner.common.optional;

public interface OptionalValue {
  boolean isEmpty();

  boolean isPresent();

  Object getAsTyped();
}
