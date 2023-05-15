package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OptionalNumber;
import org.opentripplanner.routing.api.request.preference.AccessibilityProfile;

class AccessibilityProfileReluctanceImpactTest {

  @ParameterizedTest(
    name = "Walk reluctance with accessibilityProfile={0} on edge with width={1} is {2}"
  )
  @CsvSource(
    {
      "NONE, , 1.0",
      "NONE, 100.0, 1.0",
      "NONE, 99.99, 1.0",
      "NONE, 1.4, 1.0",
      "NONE, 1.39, 1.0",
      "NONE, 1.2, 1.0",
      "NONE, 1.19, 1.0",
      "NONE, 0.90, 1.0",
      "NONE, 0.89, 1.0",
      "NONE, 0.80, 1.0",
      "NONE, 0.79, 1.0",
      "PAM, , 1.0",
      "PAM, 100.0, 3.0",
      "PAM, 99.99, 1.0",
      "PAM, 1.4, 1.0",
      "PAM, 1.39, 1.0",
      "PAM, 1.2, 1.0",
      "PAM, 1.19, 2.0",
      "PAM, 0.90, 2.0",
      "PAM, 0.89, 2.0",
      "PAM, 0.80, 2.0",
      "PAM, 0.79, 3.0",
      "UFR, , 1.0",
      "UFR, 100.0, 1.0",
      "UFR, 99.99, 1.0",
      "UFR, 1.4, 1.0",
      "UFR, 1.39, 2.0",
      "UFR, 1.2, 2.0",
      "UFR, 1.19, 3.0",
      "UFR, 0.90, 3.0",
      "UFR, 0.89, 4.0",
      "UFR, 0.80, 4.0",
      "UFR, 0.79, 5.0",
    }
  )
  void testReluctanceProcessingWithAccessibilityProfileAndWidth(
    AccessibilityProfile accessibilityProfile,
    Double edgeWidth,
    Double expectedWalkReluctance
  ) {
    assertEquals(
      expectedWalkReluctance,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withWidth(
            edgeWidth != null ? OptionalNumber.get(edgeWidth.toString()) : OptionalNumber.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }
}
