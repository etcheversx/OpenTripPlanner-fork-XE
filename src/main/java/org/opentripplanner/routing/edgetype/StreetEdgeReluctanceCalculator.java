package org.opentripplanner.routing.edgetype;

import java.util.OptionalDouble;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.routing.api.request.preference.RoutingPreferences;
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
    double reluctance = preferences.walk().reluctance();
    OptionalDouble width = edgeAccessibilityProperties.getWidth();
    if (width.isPresent() && width.getAsDouble() < preferences.walk().minimalWidth()) {
      reluctance *= 2;
    }
    OptionalBoolean lit = edgeAccessibilityProperties.getLit();
    if (lit.isPresent() && !lit.getAsBoolean() && preferences.walk().lightRequired()) {
      reluctance *= 2;
    }
    OptionalEnum surface = edgeAccessibilityProperties.getSurface();
    if (
      surface.isPresent() &&
      preferences.walk().reluctedSurfaces().contains((OSMSurface) surface.getAsEnum())
    ) {
      reluctance *= 2;
    }
    OptionalBoolean tactilePaving = edgeAccessibilityProperties.getTactilePaving();
    if (
      tactilePaving.isPresent() &&
      !tactilePaving.getAsBoolean() &&
      preferences.walk().tactilePaving()
    ) {
      reluctance *= 2;
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
