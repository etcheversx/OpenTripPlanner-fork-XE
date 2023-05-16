package org.opentripplanner.routing.edgetype;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OptionalValue;
import org.opentripplanner.routing.api.request.preference.AccessibilityProfile;

public class AccessibilityProfileReluctanceImpact {

  private static final Map<AccessibilityProfile, Map<String, Function<Object, Integer>>> impactOnReluctance = new HashMap<>();

  private static final Function<Object, Integer> widthImpactForPAM = value -> {
    Integer result = 1;
    if (value instanceof Double valueAsDouble) {
      if (valueAsDouble < 0.8) result = 3;
      else if (valueAsDouble < 1.2) result = 2;
      else if (
        valueAsDouble < 100.0
      ) result = 1;
      else result = 3;
    }
    return result;
  };

  private static final Function<Object, Integer> widthImpactForUFR = value -> {
    Integer result = 1;
    if (value instanceof Double valueAsDouble) {
      if (valueAsDouble < 0.8) result = 5;
      else if (valueAsDouble < 0.9) result = 4;
      else if (
        valueAsDouble < 1.2
      ) result = 3;
      else if (valueAsDouble < 1.4) result = 2;
      else result = 1;
    }
    return result;
  };

  private static final Function<Object, Integer> doNothing = value -> {
    return 1;
  };

  static {
    impactOnReluctance.put(AccessibilityProfile.NONE, new HashMap<>());
    impactOnReluctance.get(AccessibilityProfile.NONE).put("width", doNothing);

    impactOnReluctance.put(AccessibilityProfile.PAM, new HashMap<>());
    impactOnReluctance.get(AccessibilityProfile.PAM).put("width", widthImpactForPAM);

    impactOnReluctance.put(AccessibilityProfile.UFR, new HashMap<>());
    impactOnReluctance.get(AccessibilityProfile.UFR).put("width", widthImpactForUFR);
  }

  static double computeRegularWalkReluctanceWithAccessibilityProfile(
    double reluctance,
    AccessibilityPropertySet edgeAccessibilityProperties,
    AccessibilityProfile accessibilityProfile
  ) {
    OptionalValue<?> optionalProperty = edgeAccessibilityProperties.getProperty("width");
    if (optionalProperty.isPresent()) {
      reluctance *= impactOnReluctance.get(accessibilityProfile).get("width").apply(optionalProperty.getAsTyped());
    }
    return reluctance;
  }
}
