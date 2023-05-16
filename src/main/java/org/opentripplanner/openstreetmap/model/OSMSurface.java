package org.opentripplanner.openstreetmap.model;

import java.util.ArrayList;
import java.util.Collection;

public enum OSMSurface {
  paved,
  asphalt,
  chipseal,
  concrete,
  concrete_lanes("concrete:lane"),
  concrete_plates("concrete:plates"),
  metal,
  paving_stones,
  sett,
  cobblestone,
  unhewn_cobblestone,
  wood,
  stepping_stones,
  rubber,
  unpaved,
  compacted,
  fine_gravel,
  gravel,
  rock,
  pebblestone,
  ground,
  dirt,
  earth,
  grass,
  grass_paver,
  metal_grid,
  mud,
  sand,
  woodchips,
  snow,
  ice,
  clay,
  tartan,
  artificial_turf,
  acrylic,
  carpet;

  private final String label;

  OSMSurface (String label) {
    this.label = label;
  }

  OSMSurface() {
    this.label = null;
  }

  public static Collection<OSMSurface> parseValues(String values) {
    Collection<OSMSurface> result = new ArrayList<>();
    OptionalEnum.parseValues(values).forEach(e -> result.add((OSMSurface) e.getAsTyped()));
    return result;
  }


  @Override
  public String toString() {
    if (label != null) {
      return label;
    }
    return super.toString();
  }
}
