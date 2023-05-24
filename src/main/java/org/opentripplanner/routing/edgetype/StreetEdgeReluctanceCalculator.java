package org.opentripplanner.routing.edgetype;

import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.routing.api.request.preference.RoutingPreferences;
import org.opentripplanner.routing.api.request.preference.WalkPreferences;
import org.opentripplanner.routing.core.TraverseMode;

class StreetEdgeReluctanceCalculator {

  /** Utility class, private constructor to prevent instantiation */
  private StreetEdgeReluctanceCalculator() {}

  /**
   * Compute reluctance for a regular street section. Note! This does not apply if in a wheelchair,
   * see {@link #computeWheelchairReluctance(RoutingPreferences, double, boolean, boolean)}.
   */
  static double computeReluctance(
    RoutingPreferences pref,
    TraverseMode traverseMode,
    boolean walkingBike,
    boolean edgeIsStairs,
    AccessibilityPropertySet edgeAccessibilityProperties
  ) {
    if (edgeIsStairs) {
      return pref.walk().stairsReluctance();
    } else {
      return switch (traverseMode) {
        case WALK -> walkingBike
          ? pref.bike().walkingReluctance()
          : computeRegularWalkReluctance(pref, edgeAccessibilityProperties);
        case BICYCLE -> pref.bike().reluctance();
        case CAR -> pref.car().reluctance();
        default -> throw new IllegalArgumentException(
          "getReluctance(): Invalid mode " + traverseMode
        );
      };
    }
  }

  private static double computeRegularWalkReluctance(
    RoutingPreferences preferences,
    AccessibilityPropertySet edgeAccessibilityProperties
  ) {
    WalkPreferences walkPreferences = preferences.walk();
    double reluctance = walkPreferences.reluctance();
    if (walkPreferences.accessibilityProfile() == null) {
      reluctance =
        computeRegularWalkReluctanceWithoutAccessibilityProfile(
          reluctance,
          edgeAccessibilityProperties,
          walkPreferences
        );
    } else {
      reluctance =
        AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
          reluctance,
          edgeAccessibilityProperties,
          walkPreferences.accessibilityProfile()
        );
    }
    return reluctance;
  }

  private static double computeRegularWalkReluctanceWithoutAccessibilityProfile(
    double reluctance,
    AccessibilityPropertySet edgeAccessibilityProperties,
    WalkPreferences walkPreferences
  ) {
    var width = edgeAccessibilityProperties.getWidth();
    if (width.isPresent() && width.getAsTyped() < walkPreferences.minimalWidth()) {
      reluctance *= 2;
    }
    var lit = edgeAccessibilityProperties.getLit();
    if (lit.isPresent() && !lit.getAsTyped() && walkPreferences.lightRequired()) {
      reluctance *= 2;
    }
    var surface = edgeAccessibilityProperties.getSurface();
    if (surface.isPresent() && walkPreferences.reluctedSurfaces().contains(surface.getAsTyped())) {
      reluctance *= 2;
    }
    var tactilePaving = edgeAccessibilityProperties.getTactilePaving();
    if (
      tactilePaving.isPresent() && !tactilePaving.getAsTyped() && walkPreferences.tactilePaving()
    ) {
      reluctance *= 2;
    }
    var smoothness = edgeAccessibilityProperties.getSmoothness();
    if (smoothness.isPresent()) {
      if (walkPreferences.reluctedSmoothness().compareTo(smoothness.getAsTyped()) > 0) {
        reluctance *= 2;
      }
    }
    var incline = edgeAccessibilityProperties.getIncline();
    if (incline.isPresent()) {
      Object inclineAsObject = incline.getAsTyped();
      if (inclineAsObject instanceof Double inclineAsDouble) {
        if (Math.abs(inclineAsDouble) > Math.abs(walkPreferences.maximalIncline())) {
          reluctance *= 2;
        }
      }
    }
    var ressautMax = edgeAccessibilityProperties.getRessautMax();
    if (ressautMax.isPresent() && ressautMax.getAsTyped() > walkPreferences.ressautMax()) {
      reluctance *= 2;
    }
    var ressautMin = edgeAccessibilityProperties.getRessautMin();
    if (ressautMin.isPresent() && ressautMin.getAsTyped() < walkPreferences.ressautMin()) {
      reluctance *= 2;
    }
    var bevCtrast = edgeAccessibilityProperties.getBevCtrast();
    if (bevCtrast.isPresent() && !bevCtrast.getAsTyped() && walkPreferences.bevCtrast()) {
      reluctance *= 2;
    }
    var bevEtat = edgeAccessibilityProperties.getBevEtat();
    if (bevEtat.isPresent()) {
      if (walkPreferences.bevEtat().compareTo(bevEtat.getAsTyped()) > 0) {
        reluctance *= 2;
      }
    }
    return reluctance;
  }

  static double computeWheelchairReluctance(
    RoutingPreferences preferences,
    double maxSlope,
    boolean edgeWheelchairAccessible,
    boolean stairs
  ) {
    var wheelchair = preferences.wheelchair();
    // Add reluctance if street is not wheelchair accessible
    double reluctance = edgeWheelchairAccessible ? 1.0 : wheelchair.inaccessibleStreetReluctance();
    reluctance *= preferences.walk().reluctance();

    // Add reluctance for stairs
    if (stairs) {
      reluctance *= wheelchair.stairsReluctance();
    }

    // Add reluctance for exceeding the max slope
    double slopeExceededBy = Math.abs(maxSlope) - wheelchair.maxSlope();
    if (slopeExceededBy > 0.0) {
      double slopeExceededReluctance = wheelchair.slopeExceededReluctance();
      if (slopeExceededReluctance > 0.0) {
        // if we exceed the max slope the cost increases multiplied by how much you go over the maxSlope
        reluctance *= 1.0 + (100.0 * slopeExceededBy) * slopeExceededReluctance;
      }
    }
    return reluctance;
  }
}
