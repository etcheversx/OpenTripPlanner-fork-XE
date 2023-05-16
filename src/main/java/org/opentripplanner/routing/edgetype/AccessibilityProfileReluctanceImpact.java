package org.opentripplanner.routing.edgetype;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalValue;
import org.opentripplanner.routing.api.request.preference.AccessibilityProfile;

public class AccessibilityProfileReluctanceImpact {

  private static final Map<AccessibilityProfile, Map<String, Function<Object, Integer>>> impactOnReluctance = new HashMap<>();

  private static final Function<Object, Integer> widthImpactForPAM = value -> {
    Integer result = 1;
    if (value instanceof Double valueAsDouble) {
      if (valueAsDouble < 0.8) result = 3; else if (valueAsDouble < 1.2) result = 2; else if (
        valueAsDouble < 100.0
      ) result = 1; else result = 3;
    }
    return result;
  };

  private static final Function<Object, Integer> surfaceImpactForPAM = value -> {
    Integer result = 1;
    if (value instanceof OSMSurface valueAsOSMSurface) {
      result =
        switch (valueAsOSMSurface) {
          case paved,
            asphalt,
            chipseal,
            concrete,
            concrete_lanes,
            metal,
            rubber,
            clay,
            tartan,
            artificial_turf,
            acrylic,
            carpet -> 1;
          case concrete_plates, paving_stones, sett, wood, unpaved, compacted, fine_gravel -> 2;
          case cobblestone,
            unhewn_cobblestone,
            stepping_stones,
            gravel,
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
            ice -> 4;
          case rock -> 5;
        };
    }
    return result;
  };

  private static final Function<Object, Integer> widthImpactForUFR = value -> {
    Integer result = 1;
    if (value instanceof Double valueAsDouble) {
      if (valueAsDouble < 0.8) result = 5; else if (valueAsDouble < 0.9) result = 4; else if (
        valueAsDouble < 1.2
      ) result = 3; else if (valueAsDouble < 1.4) result = 2; else result = 1;
    }
    return result;
  };

  private static final Function<Object, Integer> surfaceImpactForUFR = value -> {
    Integer result = 1;
    if (value instanceof OSMSurface valueAsOSMSurface) {
      result =
        switch (valueAsOSMSurface) {
          case paved,
            asphalt,
            chipseal,
            concrete,
            metal,
            rubber,
            clay,
            tartan,
            acrylic,
            carpet -> 1;
          case concrete_lanes, stepping_stones, gravel, rock, mud, sand, woodchips, snow, ice -> 5;
          case concrete_plates,
            paving_stones,
            wood,
            unpaved,
            compacted,
            fine_gravel,
            artificial_turf -> 2;
          case sett, metal_grid -> 3;
          case cobblestone,
            unhewn_cobblestone,
            pebblestone,
            ground,
            dirt,
            earth,
            grass,
            grass_paver -> 4;
        };
    }
    return result;
  };

  private static final Function<Object, Integer> doNothing = value -> 1;

  static {
    impactOnReluctance.put(AccessibilityProfile.NONE, new HashMap<>());
    impactOnReluctance.get(AccessibilityProfile.NONE).put("width", doNothing);

    impactOnReluctance.put(AccessibilityProfile.PAM, new HashMap<>());
    impactOnReluctance.get(AccessibilityProfile.PAM).put("width", widthImpactForPAM);
    impactOnReluctance.get(AccessibilityProfile.PAM).put("surface", surfaceImpactForPAM);

    impactOnReluctance.put(AccessibilityProfile.UFR, new HashMap<>());
    impactOnReluctance.get(AccessibilityProfile.UFR).put("width", widthImpactForUFR);
    impactOnReluctance.get(AccessibilityProfile.UFR).put("surface", surfaceImpactForUFR);
  }

  static double computeRegularWalkReluctanceWithAccessibilityProfile(
    double reluctance,
    AccessibilityPropertySet edgeAccessibilityProperties,
    AccessibilityProfile accessibilityProfile
  ) {
    for (String propertyKey : edgeAccessibilityProperties.propertyKeys()) {
      OptionalValue<?> optionalProperty = edgeAccessibilityProperties.getProperty(propertyKey);
      if (optionalProperty.isPresent()) {
        reluctance *=
          impactOnReluctance
            .get(accessibilityProfile)
            .get(propertyKey)
            .apply(optionalProperty.getAsTyped());
      }
    }
    return reluctance;
  }
}
