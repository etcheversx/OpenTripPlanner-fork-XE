package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalNumber;
import org.opentripplanner.routing.api.request.preference.AccessibilityProfile;

class AccessibilityProfileReluctanceImpactTest {

  @ParameterizedTest(
    name = "Width impact with accessibilityProfile={0} on edge with width={1} is {2}"
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
  void testWidthImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    Double edgeWidth,
    Double expectedImpact
  ) {
    assertEquals(
      expectedImpact,
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

  @ParameterizedTest(
    name = "Surface impact with accessibilityProfile={0} on edge with surface={1} is {2}"
  )
  @CsvSource(
    {
      "NONE, , 1.0",
      "UFR, paved, 1",
      "UFR, asphalt, 1",
      "UFR, chipseal, 1",
      "UFR, concrete, 1",
      "UFR, concrete_lanes, 5",
      "UFR, concrete_plates, 2",
      "UFR, metal, 1",
      "UFR, paving_stones, 2",
      "UFR, sett, 3",
      "UFR, cobblestone, 4",
      "UFR, unhewn_cobblestone, 4",
      "UFR, wood, 2",
      "UFR, stepping_stones, 5",
      "UFR, rubber, 1",
      "UFR, unpaved, 2",
      "UFR, compacted, 2",
      "UFR, fine_gravel, 2",
      "UFR, gravel, 5",
      "UFR, rock, 5",
      "UFR, pebblestone, 4",
      "UFR, ground, 4",
      "UFR, dirt, 4",
      "UFR, earth, 4",
      "UFR, grass, 4",
      "UFR, grass_paver, 4",
      "UFR, metal_grid, 3",
      "UFR, mud, 5",
      "UFR, sand, 5",
      "UFR, woodchips, 5",
      "UFR, snow, 5",
      "UFR, ice, 5",
      "UFR, clay, 1",
      "UFR, tartan, 1",
      "UFR, artificial_turf, 2",
      "UFR, acrylic, 1",
      "UFR, carpet, 1",


    }
  )
  void testSurfaceImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    OSMSurface edgeSurface,
    Double expectedImpact
  ) throws Exception {
    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withSurface(
            edgeSurface != null ? OptionalEnum.get(edgeSurface.toString()) : OptionalEnum.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }
}
