package org.opentripplanner.openstreetmap.model;

import java.util.ArrayList;
import java.util.Collection;

public enum OSMSurface {
  paved,
  unpaved,
  concrete,
  asphalt,
  paving_stones,
  plastic,
  metal,
  wood,
  grass,
  earth,
  ground,
  gravel,
  sand;

  public static Collection<OSMSurface> parseValues(String values) {
    Collection<OSMSurface> result = new ArrayList<>();
    OptionalEnum.parseValues(values).forEach(e -> result.add((OSMSurface) e.getAsEnum()));
    return result;
  }
}
