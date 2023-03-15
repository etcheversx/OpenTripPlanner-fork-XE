package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.*;

import java.util.OptionalDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        OptionalDouble.empty()
      );
  }

  @Test
  void computedReluctanceWithWalkModeIsDefaultWalkReluctance() {
    assertEquals(WalkPreferences.DEFAULT.reluctance(), defaultWalkReluctance);
  }

  @Test
  void onAWideEnoughEdgeComputedReluctanceWithWalkModeDoesNotIncrease() {
    double walkReluctance = StreetEdgeReluctanceCalculator.computeReluctance(
      new RoutingPreferences(),
      TraverseMode.WALK,
      false,
      false,
      OptionalDouble.of(1.0)
    );

    assertEquals(defaultWalkReluctance, walkReluctance);
  }
}
