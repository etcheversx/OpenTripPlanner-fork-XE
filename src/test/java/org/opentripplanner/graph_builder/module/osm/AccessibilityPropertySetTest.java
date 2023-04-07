package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;

public class AccessibilityPropertySetTest {

  private AccessibilityPropertySet accessibilityPropertySet;

  @Test
  void testDefaultBehaviourOfBuilder() {
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty()
      );
    assertEquals(
      accessibilityPropertySet,
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty()
      )
    );
  }

  @Test
  void testWidthGetSet() {
    OptionalDouble width = OptionalDouble.of(4.5);
    accessibilityPropertySet =
      new AccessibilityPropertySet(width, OptionalBoolean.empty(), OptionalEnum.empty());
    assertEquals(width, accessibilityPropertySet.getWidth());
  }

  @Test
  void testLitGetSet() {
    OptionalBoolean lit = OptionalBoolean.of(true);
    accessibilityPropertySet =
      new AccessibilityPropertySet(OptionalDouble.empty(), lit, OptionalEnum.empty());
    assertEquals(lit, accessibilityPropertySet.getLit());
  }

  @Test
  void testSurfaceGetSet() {
    OptionalEnum surface = OptionalEnum.of(OSMSurface.paved);
    accessibilityPropertySet =
      new AccessibilityPropertySet(OptionalDouble.empty(), OptionalBoolean.empty(), surface);
    assertEquals(surface, accessibilityPropertySet.getSurface());
  }
}
