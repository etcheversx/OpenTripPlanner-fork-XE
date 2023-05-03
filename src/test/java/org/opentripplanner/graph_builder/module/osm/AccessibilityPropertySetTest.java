package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;
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
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty()
      );
    assertEquals(
      accessibilityPropertySet,
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty()
      )
    );
  }

  @Test
  void testWidthGetSet() {
    OptionalDouble width = OptionalDouble.of(4.5);
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        width,
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty()
      );
    assertEquals(width, accessibilityPropertySet.getWidth());
  }

  @Test
  void testLitGetSet() {
    OptionalBoolean lit = OptionalBoolean.of(true);
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        lit,
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty()
      );
    assertEquals(lit, accessibilityPropertySet.getLit());
  }

  @Test
  void testSurfaceGetSet() {
    OptionalEnum surface = OptionalEnum.of("paved");
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        surface,
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty()
      );
    assertEquals(surface, accessibilityPropertySet.getSurface());
  }

  @Test
  void testTactilePavingGetSet() {
    OptionalBoolean tactilePaving = OptionalBoolean.of(true);
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        tactilePaving,
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty()
      );
    assertEquals(tactilePaving, accessibilityPropertySet.getTactilePaving());
  }

  @Test
  void testSmoothnessGetSet() {
    OptionalEnum smoothness = OptionalEnum.of("intermediate");
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        smoothness,
        OptionalEnum.empty(),
        OptionalEnum.empty()
      );
    assertEquals(smoothness, accessibilityPropertySet.getSmoothness());
  }

  @Test
  void testHighwayGetSet() {
    OptionalEnum highway = OptionalEnum.of("footway");
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        highway,
        OptionalEnum.empty()
      );
    assertEquals(highway, accessibilityPropertySet.getHighway());
  }

  @Test
  void testFootwayGetSet() {
    OptionalEnum footway = OptionalEnum.of("crossing");
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        footway
      );
    assertEquals(footway, accessibilityPropertySet.getFootway());
  }
}
