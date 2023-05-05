package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;

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
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
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
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
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
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
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
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
      );
    assertEquals(lit, accessibilityPropertySet.getLit());
  }

  private static OptionalEnum optionalEnumOf(String value) {
    try {
      return OptionalEnum.get(value);
    } catch (Exception exc) {
      return OptionalEnum.empty();
    }
  }

  @Test
  void testSurfaceGetSet() {
    OptionalEnum surface = optionalEnumOf("paved");
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        surface,
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
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
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
      );
    assertEquals(tactilePaving, accessibilityPropertySet.getTactilePaving());
  }

  @Test
  void testSmoothnessGetSet() {
    OptionalEnum smoothness = optionalEnumOf("intermediate");
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        smoothness,
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
      );
    assertEquals(smoothness, accessibilityPropertySet.getSmoothness());
  }

  @Test
  void testHighwayGetSet() {
    OptionalEnum highway = optionalEnumOf("footway");
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        highway,
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
      );
    assertEquals(highway, accessibilityPropertySet.getHighway());
  }

  @Test
  void testFootwayGetSet() {
    OptionalEnum footway = optionalEnumOf("crossing");
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        footway,
        OptionalEnumAndDouble.empty(),
        OptionalDouble.empty()
      );
    assertEquals(footway, accessibilityPropertySet.getFootway());
  }

  @Test
  void testInclineGetSet() {
    OptionalEnumAndDouble incline = OptionalEnumAndDouble.empty();
    try {
      incline = OptionalEnumAndDouble.get("2.1");
    } catch (Exception exc) {
      fail("OptionalEnumAndDouble creation should not fail");
    }
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        incline,
        OptionalDouble.empty()
      );
    assertEquals(incline, accessibilityPropertySet.getIncline());
  }

  @Test
  void testTravHTrtGetSet() {
    OptionalDouble travHTrt = OptionalDouble.of(0.25);
    accessibilityPropertySet =
      new AccessibilityPropertySet(
        OptionalDouble.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalBoolean.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnum.empty(),
        OptionalEnumAndDouble.empty(),
        travHTrt
      );
    assertEquals(travHTrt, accessibilityPropertySet.getTravHTrt());
  }
}
