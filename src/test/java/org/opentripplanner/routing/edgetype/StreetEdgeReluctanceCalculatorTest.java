package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.routing.api.request.preference.RoutingPreferences;
import org.opentripplanner.routing.api.request.preference.WalkPreferences;
import org.opentripplanner.routing.core.TraverseMode;

class StreetEdgeReluctanceCalculatorTest {
  @Test
  void computedReluctanceWithWalkModeIsDefaultWalkReluctance() {
    double defaultWalkReluctance = StreetEdgeReluctanceCalculator.computeReluctance(
      new RoutingPreferences(),
      TraverseMode.WALK,
      false,
      false,
      OptionalDouble.empty(),
      OptionalBoolean.empty()
    );
    assertEquals(WalkPreferences.DEFAULT.reluctance(), defaultWalkReluctance);
  }

  private static RoutingPreferences routingPreferencesWithWidth(double minimalWidth) {
    RoutingPreferences.Builder routingPreferencesBuilder = new RoutingPreferences.Builder(
      new RoutingPreferences()
    );
    routingPreferencesBuilder.withWalk(w -> w.withMinimalWidth(minimalWidth));
    return routingPreferencesBuilder.build();
  }

  @ParameterizedTest(name = "Walk reluctance with requiredLight={0} on edge with light={1} is {2}")
  @CsvSource({
    "0.9, 1.0, 2.0",
    "0.9, 0.85, 4.0"
  })
  void testReluctanceProcessingWithWidth(Double minimalWidth, Double edgeWidth, Double expectedWalkReluctance) {
    RoutingPreferences routingPreferences = routingPreferencesWithWidth(minimalWidth);

    double walkReluctance = StreetEdgeReluctanceCalculator.computeReluctance(
      routingPreferences,
      TraverseMode.WALK,
      false,
      false,
      OptionalDouble.of(edgeWidth),
      OptionalBoolean.empty());

    assertEquals(expectedWalkReluctance, walkReluctance);
  }

  @ParameterizedTest(name = "Walk reluctance with requiredLight={0} on edge with light={1} is {2}")
  @CsvSource({
    "true, true, 2.0",
    "false, true, 2.0",
    "true, false, 4.0",
    "false, false, 2.0"
  })
  void testReluctanceProcessingWithLight(Boolean lightRequired, Boolean edgeLight, Double expectedWalkReluctance) {
    RoutingPreferences routingPreferences = routingPreferencesWithLit(lightRequired);

    double walkReluctance = StreetEdgeReluctanceCalculator.computeReluctance(
      routingPreferences,
      TraverseMode.WALK,
      false,
      false,
      OptionalDouble.empty(),
      OptionalBoolean.of(edgeLight)
    );

    assertEquals(expectedWalkReluctance, walkReluctance);
  }

  private static RoutingPreferences routingPreferencesWithLit(boolean lightRequired) {
    RoutingPreferences.Builder routingPreferencesBuilder = new RoutingPreferences.Builder(
      new RoutingPreferences()
    );
    routingPreferencesBuilder.withWalk(w -> w.withLightRequired(lightRequired));
    return routingPreferencesBuilder.build();
  }
}
