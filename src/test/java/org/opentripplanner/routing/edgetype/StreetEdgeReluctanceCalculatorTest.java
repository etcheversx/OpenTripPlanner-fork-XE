package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.OptionalDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.routing.api.request.preference.RoutingPreferences;
import org.opentripplanner.routing.api.request.preference.WalkPreferences;
import org.opentripplanner.routing.core.TraverseMode;

class StreetEdgeReluctanceCalculatorTest {

  private double defaultWalkReluctance;

  @BeforeEach
  void setup() {
    defaultWalkReluctance =
      StreetEdgeReluctanceCalculator.computeReluctance(
        new RoutingPreferences(),
        TraverseMode.WALK,
        false,
        false,
        OptionalDouble.empty(),
        OptionalBoolean.empty()
      );
  }

  @Test
  void computedReluctanceWithWalkModeIsDefaultWalkReluctance() {
    assertEquals(WalkPreferences.DEFAULT.reluctance(), defaultWalkReluctance);
  }

  @Test
  void onAWideEnoughEdgeComputedReluctanceWithWalkModeDoesNotIncrease() {
    RoutingPreferences routingPreferences = routingPreferencesWithWidth(0.9);

    double walkReluctance = StreetEdgeReluctanceCalculator.computeReluctance(
      routingPreferences,
      TraverseMode.WALK,
      false,
      false,
      OptionalDouble.of(1.0),
      OptionalBoolean.empty());

    assertEquals(defaultWalkReluctance, walkReluctance);
  }

  @Test
  void onAToNarrowEdgeComputedReluctanceWithWalkModeIncreases() {
    RoutingPreferences routingPreferences = routingPreferencesWithWidth(0.9);

    double walkReluctance = StreetEdgeReluctanceCalculator.computeReluctance(
      routingPreferences,
      TraverseMode.WALK,
      false,
      false,
      OptionalDouble.of(0.8),
      OptionalBoolean.empty());

    assertEquals(defaultWalkReluctance * 2, walkReluctance);
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
    "true, true, 2.0",
    "false, true, 2.0",
    "true, false, 4.0",
    "false, false, 2.0"
  })
  void testReluctanceProcessingWithLight(boolean lightRequired, boolean edgeLight, double expectedWalkReluctance) {
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
