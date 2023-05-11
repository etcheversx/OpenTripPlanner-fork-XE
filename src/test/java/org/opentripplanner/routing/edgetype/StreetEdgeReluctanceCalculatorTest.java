package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.OptionalDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OSMSmoothness;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;
import org.opentripplanner.openstreetmap.model.OptionalNumber;
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
        new AccessibilityPropertySet(
          edgeWidth != null ? OptionalNumber.get(edgeWidth.toString()) : OptionalNumber.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnumAndDouble.empty(),
          OptionalDouble.empty()
        )
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
        new AccessibilityPropertySet(
          OptionalNumber.empty(),
          edgeLight != null ? OptionalBoolean.of(edgeLight) : OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnumAndDouble.empty(),
          OptionalDouble.empty()
        )
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

      OptionalEnum edgeSurface = edgeSurfaceString != null
        ? OptionalEnum.get(edgeSurfaceString)
        : OptionalEnum.empty();

      assertEquals(
        expectedWalkReluctance,
        computeWalkReluctance(
          new AccessibilityPropertySet(
            OptionalNumber.empty(),
            OptionalBoolean.empty(),
            edgeSurface,
            OptionalBoolean.empty(),
            OptionalEnum.empty(),
            OptionalEnum.empty(),
            OptionalEnum.empty(),
            OptionalEnumAndDouble.empty(),
            OptionalDouble.empty()
          )
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
        new AccessibilityPropertySet(
          OptionalNumber.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty(),
          edgeTactilePaving != null
            ? OptionalBoolean.of(edgeTactilePaving)
            : OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnumAndDouble.empty(),
          OptionalDouble.empty()
        )
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

      OptionalEnum edgeSmoothness = edgeSmoothnessString != null
        ? OptionalEnum.get(edgeSmoothnessString)
        : OptionalEnum.empty();

      assertEquals(
        expectedWalkReluctance,
        computeWalkReluctance(
          new AccessibilityPropertySet(
            OptionalNumber.empty(),
            OptionalBoolean.empty(),
            OptionalEnum.empty(),
            OptionalBoolean.empty(),
            edgeSmoothness,
            OptionalEnum.empty(),
            OptionalEnum.empty(),
            OptionalEnumAndDouble.empty(),
            OptionalDouble.empty()
          )
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
        new AccessibilityPropertySet(
          OptionalNumber.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          edgeIncline != null
            ? OptionalEnumAndDouble.get(edgeIncline)
            : OptionalEnumAndDouble.empty(),
          OptionalDouble.empty()
        )
      )
    );
  }

  @ParameterizedTest(
    name = "Walk reluctance with maximalTravHTrt={0} on edge with trav_h_trt={1} is {2}"
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
  void testReluctanceProcessingWithTravHTrt(
    Double maximalTravHTrt,
    Double edgeTravHTrt,
    Double expectedWalkReluctance
  ) {
    if (maximalTravHTrt != null) {
      routingPreferencesBuilder.withWalk(w -> w.withMaximalTravHTrt(maximalTravHTrt));
    }

    assertEquals(
      expectedWalkReluctance,
      computeWalkReluctance(
        new AccessibilityPropertySet(
          OptionalNumber.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnum.empty(),
          OptionalEnumAndDouble.empty(),
          edgeTravHTrt != null ? OptionalDouble.of(edgeTravHTrt) : OptionalDouble.empty()
        )
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
}
