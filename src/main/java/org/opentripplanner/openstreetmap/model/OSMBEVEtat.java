package org.opentripplanner.openstreetmap.model;

public enum OSMBEVEtat {
  no,
  bad,
  yes;

  public static OSMBEVEtat weakValueOf(String s) {
    try {
      return OSMBEVEtat.valueOf(s);
    } catch (Exception e) {
      return null;
    }
  }
}
