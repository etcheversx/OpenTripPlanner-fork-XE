package org.opentripplanner.routing.edgetype;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OptionalNumber;
import org.opentripplanner.routing.api.request.preference.AccessibilityProfile;

public class AccessibilityProfileReluctanceImpact {
  private static final Map<AccessibilityProfile, Map<String, Function<Object, Integer>>> impactOnReluctance = new HashMap<>();

  private static final Function<Object, Integer> widthImpactOnReluctance = value -> {
    Integer result = 1;
    if (value instanceof Double valueAsDouble) {
      if (valueAsDouble < 0.8) result = 3;
      else if (valueAsDouble < 1.2) result = 2;
      else if (valueAsDouble < 100.0) result = 1;
      else result = 3;
    }
    return result;
  };

  static {
    impactOnReluctance.put(AccessibilityProfile.PAM, new HashMap<>());
    impactOnReluctance.get(AccessibilityProfile.PAM).put("width", widthImpactOnReluctance);
  }

  static double computeRegularWalkReluctanceWithAccessibilityProfile(
    double reluctance,
    AccessibilityPropertySet edgeAccessibilityProperties,
    AccessibilityProfile accessibilityProfile
  ) {
    OptionalNumber optionalWidth = edgeAccessibilityProperties.getWidth();
    if (optionalWidth.isPresent()) {
      Double width = optionalWidth.getAsTyped();
      reluctance *= impactOnReluctance.get(accessibilityProfile).get("width").apply(width);
    }
    return reluctance;
  }


}
