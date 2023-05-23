package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OSMBEVEtat;
import org.opentripplanner.openstreetmap.model.OSMHighway;
import org.opentripplanner.openstreetmap.model.OSMIncline;
import org.opentripplanner.openstreetmap.model.OSMSmoothness;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;
import org.opentripplanner.openstreetmap.model.OptionalNumber;
import org.opentripplanner.routing.api.request.preference.AccessibilityProfile;

class AccessibilityProfileReluctanceImpactTest {

  @ParameterizedTest(
    name = "Width impact with accessibilityProfile={0} on edge with width={1} is {2}"
  )
  @CsvSource(
    {
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
      "PAM, , 1.0",
      "PAM, paved, 1",
      "PAM, asphalt, 1",
      "PAM, chipseal, 1",
      "PAM, concrete, 1",
      "PAM, concrete_lanes, 1",
      "PAM, concrete_plates, 2",
      "PAM, metal, 1",
      "PAM, paving_stones, 2",
      "PAM, sett, 2",
      "PAM, cobblestone, 4",
      "PAM, unhewn_cobblestone, 4",
      "PAM, wood, 2",
      "PAM, stepping_stones, 4",
      "PAM, rubber, 1",
      "PAM, unpaved, 2",
      "PAM, compacted, 2",
      "PAM, fine_gravel, 2",
      "PAM, gravel, 4",
      "PAM, rock, 5",
      "PAM, pebblestone, 4",
      "PAM, ground, 4",
      "PAM, dirt, 4",
      "PAM, earth, 4",
      "PAM, grass, 4",
      "PAM, grass_paver, 4",
      "PAM, metal_grid, 4",
      "PAM, mud, 4",
      "PAM, sand, 4",
      "PAM, woodchips, 4",
      "PAM, snow, 4",
      "PAM, ice, 4",
      "PAM, clay, 1",
      "PAM, tartan, 1",
      "PAM, artificial_turf, 1",
      "PAM, acrylic, 1",
      "PAM, carpet, 1",
      "UFR, , 1.0",
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
            edgeSurface != null
              ? OptionalEnum.get(edgeSurface.name(), OSMSurface.class)
              : OptionalEnum.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }

  @ParameterizedTest(
    name = "Smoothness impact with accessibilityProfile={0} on edge with smoothness={1} is {2}"
  )
  @CsvSource(
    {
      "PAM, , 1.0",
      "PAM, impassable, 5.0",
      "PAM, very_horrible, 5.0",
      "PAM, horrible, 5.0",
      "PAM, very_bad, 4.0",
      "PAM, bad, 3.0",
      "PAM, intermediate, 2.0",
      "PAM, good, 1.0",
      "PAM, excellent, 1.0",
      "UFR, , 1.0",
      "UFR, impassable, 5.0",
      "UFR, very_horrible, 5.0",
      "UFR, horrible, 5.0",
      "UFR, very_bad, 5.0",
      "UFR, bad, 5.0",
      "UFR, intermediate, 4.0",
      "UFR, good, 1.0",
      "UFR, excellent, 1.0",
    }
  )
  void testSmoothnessImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    OSMSmoothness edgeSmoothness,
    Double expectedImpact
  ) throws Exception {
    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withSmoothness(
            edgeSmoothness != null
              ? OptionalEnum.get(edgeSmoothness.toString(), OSMSmoothness.class)
              : OptionalEnum.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }

  @ParameterizedTest(
    name = "Incline impact with accessibilityProfile={0} on edge with incline={1} is {2}"
  )
  @CsvSource(
    {
      "PAM, , 1.0",
      "PAM, up, 1.0",
      "PAM, down, 1.0",
      "PAM, 4.0, 1.0",
      "PAM, 4.01, 2.0",
      "PAM, 5.0, 2.0",
      "PAM, 5.01, 2.0",
      "PAM, 6.0, 2.0",
      "PAM, 6.01, 2.0",
      "PAM, 7.0, 2.0",
      "PAM, 7.01, 3.0",
      "PAM, 8.0, 3.0",
      "PAM, 8.01, 3.0",
      "PAM, 9.0, 3.0",
      "PAM, 9.01, 3.0",
      "PAM, 10.0, 3.0",
      "PAM, 10.01, 3.0",
      "PAM, 11.0, 3.0",
      "PAM, 11.01, 4.0",
      "PAM, 12.0, 4.0",
      "PAM, 12.01, 4.0",
      "UFR, , 1.0",
      "UFR, up, 1.0",
      "UFR, down, 1.0",
      "UFR, 4.0, 1.0",
      "UFR, 4.01, 2.0",
      "UFR, 5.0, 2.0",
      "UFR, 5.01, 3.0",
      "UFR, 6.0, 3.0",
      "UFR, 6.01, 4.0",
      "UFR, 7.0, 4.0",
      "UFR, 7.01, 5.0",
      "UFR, 8.0, 5.0",
      "UFR, 8.01, 5.0",
      "UFR, 9.0, 5.0",
      "UFR, 9.01, 5.0",
      "UFR, 10.0, 5.0",
      "UFR, 10.01, 5.0",
      "UFR, 11.0, 5.0",
      "UFR, 11.01, 5.0",
      "UFR, 12.0, 5.0",
      "UFR, 12.01, 5.0",
    }
  )
  void testInclineImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    String edgeIncline,
    Double expectedImpact
  ) throws Exception {
    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withIncline(
            edgeIncline != null
              ? OptionalEnumAndDouble.get(edgeIncline, OSMIncline.class)
              : OptionalEnumAndDouble.empty()
          )
          .build(),
        accessibilityProfile
      )
    );

    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withIncline(
            (edgeIncline != null && !"up".equals(edgeIncline) && !"down".equals(edgeIncline))
              ? OptionalEnumAndDouble.get(edgeIncline, OSMIncline.class)
              : OptionalEnumAndDouble.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }

  @ParameterizedTest(
    name = "Highway impact with accessibilityProfile={0} on edge with highway={1} is {2}"
  )
  @CsvSource(
    {
      "PAM, , 1.0",
      "PAM, pedestrian, 1.0",
      "PAM, footway, 1.0",
      "PAM, steps, 2.0",
      "PAM, elevator, 1.0",
      "PAM, corridor, 1.0",
      "PAM, residential, 1.0",
      "UFR, , 1.0",
      "UFR, pedestrian, 1.0",
      "UFR, footway, 1.0",
      "UFR, steps, 5.0",
      "UFR, elevator, 1.0",
      "UFR, corridor, 1.0",
      "UFR, residential, 1.0",
    }
  )
  void testHighwayImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    OSMHighway edgeHighway,
    Double expectedImpact
  ) throws Exception {
    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withHighway(
            edgeHighway != null
              ? OptionalEnum.get(edgeHighway.toString(), OSMHighway.class)
              : OptionalEnum.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }

  @ParameterizedTest(
    name = "RessautMax impact with accessibilityProfile={0} on edge with wgt:ressaut_max={1} is {2}"
  )
  @CsvSource(
    {
      "PAM, , 1.0",
      "PAM, 0.0, 3.0",
      "PAM, 0.001, 1.0",
      "PAM, 0.02, 1.0",
      "PAM, 0.021, 2.0",
      "PAM, 0.04, 2.0",
      "PAM, 0.041, 2.0",
      "PAM, 0.06, 2.0",
      "PAM, 0.061, 3.0",
      "PAM, 0.12, 3.0",
      "PAM, 0.121, 3.0",
      "UFR, , 1.0",
      "UFR, 0.0, 1.0",
      "UFR, 0.001, 1.0",
      "UFR, 0.02, 1.0",
      "UFR, 0.021, 2.0",
      "UFR, 0.04, 2.0",
      "UFR, 0.041, 3.0",
      "UFR, 0.06, 3.0",
      "UFR, 0.061, 4.0",
      "UFR, 0.12, 4.0",
      "UFR, 0.121, 5.0",
    }
  )
  void testRessautMaxImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    Double edgeRessautMax,
    Double expectedImpact
  ) {
    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withRessautMax(
            edgeRessautMax != null
              ? OptionalNumber.get(edgeRessautMax.toString())
              : OptionalNumber.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }

  @ParameterizedTest(
    name = "RessautMin impact with accessibilityProfile={0} on edge with wgt:ressaut_min={1} is {2}"
  )
  @CsvSource(
    {
      "PAM, , 1.0",
      "PAM, 0.0, 3.0",
      "PAM, 0.001, 1.0",
      "UFR, , 1.0",
      "UFR, 0.0, 1.0",
      "UFR, 0.001, 1.0",
    }
  )
  void testRessautMinImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    Double edgeRessautMin,
    Double expectedImpact
  ) {
    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withRessautMin(
            edgeRessautMin != null
              ? OptionalNumber.get(edgeRessautMin.toString())
              : OptionalNumber.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }

  @ParameterizedTest(
    name = "BevEtat impact with accessibilityProfile={0} on edge with wgt:bev_etat={1} is {2}"
  )
  @CsvSource(
    {
      "PAM, , 1.0",
      "PAM, no, 4.0",
      "PAM, bad, 2.0",
      "PAM, yes, 1.0",
      "UFR, , 1.0",
      "UFR, no, 1.0",
      "UFR, bad, 1.0",
      "UFR, yes, 1.0",
    }
  )
  void testBevEtatImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    String edgeBevEtat,
    Double expectedImpact
  ) throws Exception {
    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withBevEtat(
            edgeBevEtat != null
              ? OptionalEnum.get(edgeBevEtat, OSMBEVEtat.class)
              : OptionalEnum.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }

  @ParameterizedTest(
    name = "BevCtrast impact with accessibilityProfile={0} on edge with wgt:bev_ctrast={1} is {2}"
  )
  @CsvSource(
    {
      "PAM, , 1.0",
      "PAM, false, 1.0",
      "PAM, true, 3.0",
      "UFR, , 1.0",
      "UFR, false, 1.0",
      "UFR, true, 1.0",
    }
  )
  void testBevCtrastImpactOnReluctanceWithAccessibilityProfile(
    AccessibilityProfile accessibilityProfile,
    Boolean edgeBevCtrast,
    Double expectedImpact
  ) {
    assertEquals(
      expectedImpact,
      AccessibilityProfileReluctanceImpact.computeRegularWalkReluctanceWithAccessibilityProfile(
        1.0,
        new AccessibilityPropertySet.Builder()
          .withBevCtrast(
            edgeBevCtrast != null ? OptionalBoolean.of(edgeBevCtrast) : OptionalBoolean.empty()
          )
          .build(),
        accessibilityProfile
      )
    );
  }
}
