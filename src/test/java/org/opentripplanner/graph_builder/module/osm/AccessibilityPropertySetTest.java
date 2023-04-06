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

  @Test
  void testWidthGetSet() {
    AccessibilityPropertySet accessibilityPropertySet;

    OptionalDouble width = OptionalDouble.of(4.5);
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withWidth(width).build();
    assertEquals(width, accessibilityPropertySet.getWidth());
  }

  @Test
  void testLitGetSet() {
    AccessibilityPropertySet accessibilityPropertySet;

    OptionalBoolean lit = OptionalBoolean.of(true);
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withLit(lit).build();
    assertEquals(lit, accessibilityPropertySet.getLit());
  }
}