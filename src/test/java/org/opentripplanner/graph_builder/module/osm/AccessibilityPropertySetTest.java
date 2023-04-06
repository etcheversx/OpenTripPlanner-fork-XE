package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;

public class AccessibilityPropertySetTest {
  @Test
  void testDefaultBehaviourOfBuilder() {

    AccessibilityPropertySet expectedValue = new AccessibilityPropertySet.Builder()
      .withWidth(OptionalDouble.empty())
      .withLit(OptionalBoolean.empty())
      .withSurface(OptionalEnum.empty())
      .build();
    assertEquals(expectedValue, new AccessibilityPropertySet.Builder().build());
  }
}