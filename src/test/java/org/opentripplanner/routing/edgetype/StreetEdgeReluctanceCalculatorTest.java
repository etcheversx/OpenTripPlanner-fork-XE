package org.opentripplanner.routing.edgetype;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.opentripplanner.routing.api.request.preference.RoutingPreferences;
import org.opentripplanner.routing.api.request.preference.WalkPreferences;
import org.opentripplanner.routing.core.TraverseMode;

class StreetEdgeReluctanceCalculatorTest {

  private double doubleAccuracy = 0.0000000001;

  @Test
  void computedReluctanceWithWalkModeIsDefaultWalkReluctance() {
    double defaultWalkReluctance = StreetEdgeReluctanceCalculator.computeReluctance(
      new RoutingPreferences(),
      TraverseMode.WALK,
      false,
      false
    );

    assertEquals(WalkPreferences.DEFAULT.reluctance(), defaultWalkReluctance, doubleAccuracy);
  }
}
