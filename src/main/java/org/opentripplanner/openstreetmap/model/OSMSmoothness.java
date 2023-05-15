package org.opentripplanner.openstreetmap.model;

public enum OSMSmoothness {
  impassable,
  very_horrible,
  horrible,
  very_bad,
  bad,
  intermediate,
  good,
  excellent;

  public static OSMSmoothness weakValueOf(String s) {
    try {
      return OSMSmoothness.valueOf(s);
    } catch (Exception e) {
      return null;
    }
  }
}
