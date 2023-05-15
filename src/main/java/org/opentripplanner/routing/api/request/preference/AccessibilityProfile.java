package org.opentripplanner.routing.api.request.preference;

public enum AccessibilityProfile {
  NONE,
  PAM,
  UFR;

  public static AccessibilityProfile weakValueOf(String s) {
    try {
      return AccessibilityProfile.valueOf(s);
    } catch (Exception e) {
      return null;
    }
  }
}
