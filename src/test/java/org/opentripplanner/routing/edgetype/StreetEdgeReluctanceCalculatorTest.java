package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.common.optional.OptionalBoolean;
import org.opentripplanner.common.optional.OptionalEnum;
import org.opentripplanner.common.optional.OptionalEnumAndDouble;
import org.opentripplanner.common.optional.OptionalNumber;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OSMBEVEtat;
import org.opentripplanner.openstreetmap.model.OSMHighway;
import org.opentripplanner.openstreetmap.model.OSMIncline;
import org.opentripplanner.openstreetmap.model.OSMSmoothness;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.routing.api.request.preference.AccessibilityProfile;
import org.opentripplanner.routing.api.request.preference.RoutingPreferences;
import org.opentripplanner.routing.api.request.preference.WalkPreferences;
import org.opentripplanner.routing.core.TraverseMode;

class StreetEdgeReluctanceCalculatorTest {

  private RoutingPreferences.Builder routingPreferencesBuilder;

  @BeforeEach
  void setup() {
    routingPreferencesBuilder = new RoutingPreferences.Builder(new RoutingPreferences());
  }

  @Test
  void computedReluctanceWithWalkModeIsDefaultWalkReluctance() {
    assertEquals(2.0, WalkPreferences.DEFAULT.reluctance());
  }

  @ParameterizedTest(name = "Walk reluctance with minimalWidth={0} on edge with width={1} is {2}")
  @CsvSource({ ", , 2.0", "0.9, , 2.0", ", 1.0, 2.0", "0.9, 1.0, 2.0", "0.9, 0.85, 4.0" })
  void testReluctanceProcessingWithWidth(
    Double minimalWidth,
    Double edgeWidth,
    Double expectedWalkReluctance
  ) {
    if (minimalWidth != null) {
      routingPreferencesBuilder.withWalk(w -> w.withMinimalWidth(minimalWidth));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withWidth(
            edgeWidth != null ? OptionalNumber.get(edgeWidth.toString()) : OptionalNumber.empty()
          )
          .build()
      )
    );
  }

  @ParameterizedTest(name = "Walk reluctance with requiredLight={0} on edge with light={1} is {2}")
  @CsvSource(
    {
      ", , 2.0",
      "true, , 2.0",
      "false, , 2.0",
      ", true, 2.0",
      ", false, 2.0",
      "true, true, 2.0",
      "false, true, 2.0",
      "true, false, 4.0",
      "false, false, 2.0",
    }
  )
  void testReluctanceProcessingWithLight(
    Boolean lightRequired,
    Boolean edgeLight,
    Double expectedWalkReluctance
  ) {
    if (lightRequired != null) {
      routingPreferencesBuilder.withWalk(w -> w.withLightRequired(lightRequired));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withLit(edgeLight != null ? OptionalBoolean.of(edgeLight) : OptionalBoolean.empty())
          .build()
      )
    );
  }

  @ParameterizedTest(
    name = "Walk reluctance with reluctedSurfaces={0} on edge with surface={1} is {2}"
  )
  @CsvSource(
    {
      ", , 2.0",
      "sand, , 2.0",
      ", sand, 2.0",
      "grass, sand, 2.0",
      "sand, sand, 4.0",
      "sand;grass, grass, 4.0",
    }
  )
  void testReluctanceProcessingWithSurface(
    String reluctedSurfacesString,
    String edgeSurfaceString,
    Double expectedWalkReluctance
  ) {
    try {
      if (reluctedSurfacesString != null) {
        routingPreferencesBuilder.withWalk(w ->
          w.withReluctedSurfaces(OSMSurface.parseValues(reluctedSurfacesString))
        );
      }

      OptionalEnum<OSMSurface> edgeSurface = edgeSurfaceString != null
        ? OptionalEnum.get(edgeSurfaceString, OSMSurface.class)
        : OptionalEnum.empty();

      assertEquals(
        expectedWalkReluctance,
        computeWalkReluctance(
          new AccessibilityPropertySet.Builder().withSurface(edgeSurface).build()
        )
      );
    } catch (Exception exc) {
      fail("Unexpected exception : " + exc.getMessage());
    }
  }

  @ParameterizedTest(name = "Walk reluctance with requiredLight={0} on edge with light={1} is {2}")
  @CsvSource(
    {
      ", , 2.0",
      "true, , 2.0",
      "false, , 2.0",
      ", true, 2.0",
      ", false, 2.0",
      "true, true, 2.0",
      "false, true, 2.0",
      "true, false, 4.0",
      "false, false, 2.0",
    }
  )
  void testReluctanceProcessingWithTactilePaving(
    Boolean tactilePavingRequired,
    Boolean edgeTactilePaving,
    Double expectedWalkReluctance
  ) {
    if (tactilePavingRequired != null) {
      routingPreferencesBuilder.withWalk(w -> w.withTactilePaving(tactilePavingRequired));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withTactilePaving(
            edgeTactilePaving != null
              ? OptionalBoolean.of(edgeTactilePaving)
              : OptionalBoolean.empty()
          )
          .build()
      )
    );
  }

  @ParameterizedTest(
    name = "Walk reluctance with reluctedSmoothness={0} on edge with smoothness={1} is {2}"
  )
  @CsvSource(
    {
      ", , 2.0",
      "intermediate, , 2.0",
      ", intermediate, 2.0",
      "good, very_bad, 4.0",
      "intermediate, intermediate, 2.0",
      "very_bad, bad, 2.0",
    }
  )
  void testReluctanceProcessingWithSmoothness(
    String reluctedSmoothnessString,
    String edgeSmoothnessString,
    Double expectedWalkReluctance
  ) {
    try {
      if (reluctedSmoothnessString != null) {
        routingPreferencesBuilder.withWalk(w ->
          w.withReluctedSmoothness(OSMSmoothness.valueOf(reluctedSmoothnessString))
        );
      }

      OptionalEnum<OSMSmoothness> edgeSmoothness = edgeSmoothnessString != null
        ? OptionalEnum.get(edgeSmoothnessString, OSMSmoothness.class)
        : OptionalEnum.empty();

      assertEquals(
        expectedWalkReluctance,
        computeWalkReluctance(
          new AccessibilityPropertySet.Builder().withSmoothness(edgeSmoothness).build()
        )
      );
    } catch (Exception exc) {
      fail("Unexpected exception : " + exc.getMessage());
    }
  }

  @ParameterizedTest(
    name = "Walk reluctance with maximalIncline={0} on edge with incline={1} is {2}"
  )
  @CsvSource(
    {
      ", , 2.0",
      "2.0, , 2.0",
      "2.0, up, 2.0",
      "2.0, down, 2.0",
      ", 1.0, 2.0",
      "1.25, 1.0, 2.0",
      "-1.25, 1.0, 2.0",
      "1.25, -1.0, 2.0",
      "-1.25, -1.0, 2.0",
      "1.25, 2.0, 4.0",
      "-1.25, 2.0, 4.0",
      "1.25, -2.0, 4.0",
      "-1.25, -2.0, 4.0",
    }
  )
  void testReluctanceProcessingWithIncline(
    Double maximalIncline,
    String edgeIncline,
    Double expectedWalkReluctance
  ) throws Exception {
    if (maximalIncline != null) {
      routingPreferencesBuilder.withWalk(w -> w.withMaximalIncline(maximalIncline));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withIncline(
            edgeIncline != null
              ? OptionalEnumAndDouble.get(edgeIncline, OSMIncline.class)
              : OptionalEnumAndDouble.empty()
          )
          .build()
      )
    );
  }

  @ParameterizedTest(
    name = "Walk reluctance with ressautMax={0} on edge with wgt:ressaut_max={1} is {2}"
  )
  @CsvSource(
    {
      ", , 2.0",
      "0.2, , 2.0",
      ", 0.17, 2.0",
      "0.19, 0.17, 2.0",
      "0.17, 0.17, 2.0",
      "0.15, 0.17, 4.0",
    }
  )
  void testReluctanceProcessingWithRessautMax(
    Double ressautMax,
    Double edgeRessautMax,
    Double expectedWalkReluctance
  ) {
    if (ressautMax != null) {
      routingPreferencesBuilder.withWalk(w -> w.withRessautMax(ressautMax));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withRessautMax(
            edgeRessautMax != null
              ? OptionalNumber.get(edgeRessautMax.toString())
              : OptionalNumber.empty()
          )
          .build()
      )
    );
  }

  @ParameterizedTest(
    name = "Walk reluctance with ressautMin={0} on edge with wgt:ressaut_min={1} is {2}"
  )
  @CsvSource(
    {
      ", , 2.0",
      "0.02, , 2.0",
      ", 0.03, 2.0",
      "0.02, 0.03, 2.0",
      "0.03, 0.03, 2.0",
      "0.04, 0.03, 4.0",
    }
  )
  void testReluctanceProcessingWithRessautMin(
    Double ressautMin,
    Double edgeRessautMin,
    Double expectedWalkReluctance
  ) {
    if (ressautMin != null) {
      routingPreferencesBuilder.withWalk(w -> w.withRessautMin(ressautMin));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withRessautMin(
            edgeRessautMin != null
              ? OptionalNumber.get(edgeRessautMin.toString())
              : OptionalNumber.empty()
          )
          .build()
      )
    );
  }

  @ParameterizedTest(name = "Walk reluctance with bevEtat={0} on edge with bevEtat={1} is {2}")
  @CsvSource(
    {
      ", , 2.0",
      "no, , 2.0",
      "bad, , 2.0",
      "yes, , 2.0",
      ", no, 2.0",
      ", bad, 2.0",
      ", yes, 2.0",
      "no, no, 2.0",
      "no, bad, 2.0",
      "no, yes, 2.0",
      "bad, no, 4.0",
      "bad, bad, 2.0",
      "bad, yes, 2.0",
      "yes, no, 4.0",
      "yes, bad, 4.0",
      "yes, yes, 2.0",
    }
  )
  void testReluctanceProcessingWithBevEtat(
    OSMBEVEtat bevEtat,
    String edgeBevEtat,
    Double expectedWalkReluctance
  ) throws Exception {
    if (bevEtat != null) {
      routingPreferencesBuilder.withWalk(w -> w.withBevEtat(bevEtat));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withBevEtat(
            edgeBevEtat != null
              ? OptionalEnum.get(edgeBevEtat, OSMBEVEtat.class)
              : OptionalEnum.empty()
          )
          .build()
      )
    );
  }

  @ParameterizedTest(name = "Walk reluctance with bevCtrast={0} on edge with bevCtrast={1} is {2}")
  @CsvSource(
    {
      ", , 2.0",
      "true, , 2.0",
      "false, , 2.0",
      ", true, 2.0",
      ", false, 2.0",
      "true, true, 2.0",
      "false, true, 2.0",
      "true, false, 4.0",
      "false, false, 2.0",
    }
  )
  void testReluctanceProcessingWithBevCtrast(
    Boolean bevCtrast,
    Boolean edgeBevCtrast,
    Double expectedWalkReluctance
  ) {
    if (bevCtrast != null) {
      routingPreferencesBuilder.withWalk(w -> w.withBevCtrast(bevCtrast));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withBevCtrast(
            edgeBevCtrast != null ? OptionalBoolean.of(edgeBevCtrast) : OptionalBoolean.empty()
          )
          .build()
      )
    );
  }

  @ParameterizedTest(name = "Walk reluctance with reluctanceOnHighway={0} on edge with highway={1} is {2}")
  @CsvSource(
    {
      ", , 2.0",
      ", pedestrian, 2.0",
      ", footway, 2.0",
      ", steps, 2.0",
      ", elevator, 2.0",
      ", corridor, 2.0",
      ", residential, 2.0",
      ", unclassified, 2.0",
      "false, , 2.0",
      "false, pedestrian, 2.0",
      "false, footway, 2.0",
      "false, steps, 2.0",
      "false, elevator, 2.0",
      "false, corridor, 2.0",
      "false, residential, 2.0",
      "false, unclassified, 2.0",
      "true, , 2.0",
      "true, pedestrian, 2.0",
      "true, footway, 2.0",
      "true, steps, 2.0",
      "true, elevator, 2.0",
      "true, corridor, 2.0",
      "true, residential, 4.0",
      "true, unclassified, 4.0",
    }
  )
  void testReluctanceProcessingWithHighway(
    Boolean reluctanceOnHighway,
    String edgeHighway,
    Double expectedWalkReluctance
  ) throws Exception {
    if (reluctanceOnHighway != null) {
      routingPreferencesBuilder.withWalk(w -> w.withReluctanceOnHighway(reluctanceOnHighway));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet.Builder()
          .withHighway(
            edgeHighway != null ? OptionalEnum.get(edgeHighway, OSMHighway.class) : OptionalEnum.empty()
          )
          .build()
      )
    );
  }

  private double computeWalkReluctance(AccessibilityPropertySet edgeAccessibilityProperties) {
    return StreetEdgeReluctanceCalculator.computeReluctance(
      routingPreferencesBuilder.build(),
      TraverseMode.WALK,
      false,
      false,
      edgeAccessibilityProperties
    );
  }

  @Test
  void testAccessibilityProfileOverrideAnyOtherAccessibilityPreference() {
    AccessibilityPropertySet accessibilityPropertySet = new AccessibilityPropertySet.Builder()
      .withWidth(OptionalNumber.get("0.75"))
      .build();
    routingPreferencesBuilder.withWalk(w -> w.withMinimalWidth(0.9));

    assertEquals(4.0, computeWalkReluctance(accessibilityPropertySet));

    routingPreferencesBuilder.withWalk(w ->
      w.withMinimalWidth(0.9).withAccessibilityProfile(AccessibilityProfile.PAM)
    );

    assertEquals(6.0, computeWalkReluctance(accessibilityPropertySet));
  }
}
