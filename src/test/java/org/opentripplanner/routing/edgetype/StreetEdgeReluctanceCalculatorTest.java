package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.OptionalDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
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

  @ParameterizedTest(name = "Walk reluctance with requiredLight={0} on edge with light={1} is {2}")
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
          edgeWidth != null ? OptionalDouble.of(edgeWidth) : OptionalDouble.empty(),
          OptionalBoolean.empty(),
          OptionalEnum.empty()
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
          OptionalDouble.empty(),
          edgeLight != null ? OptionalBoolean.of(edgeLight) : OptionalBoolean.empty(),
          OptionalEnum.empty()
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
