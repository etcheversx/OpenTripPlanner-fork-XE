package org.opentripplanner.openstreetmap.model;

import java.util.ArrayList;
import java.util.Collection;

public enum OSMSmoothness {
  excellent,
  good,
  intermediate,
  bad,
  very_bad;

  public static Collection<OSMSmoothness> parseValues(String values) {
    Collection<OSMSmoothness> result = new ArrayList<>();
    OptionalEnum.parseValues(values).forEach(e -> result.add((OSMSmoothness) e.getAsEnum()));
    return result;
  }
}
