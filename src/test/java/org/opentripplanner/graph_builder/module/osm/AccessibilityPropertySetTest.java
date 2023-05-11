package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;
import org.opentripplanner.openstreetmap.model.OptionalNumber;

public class AccessibilityPropertySetTest {

  private AccessibilityPropertySet accessibilityPropertySet;

  @Test
  void testDefaultBehaviourOfBuilder() {
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().build();
    assertEquals(accessibilityPropertySet, new AccessibilityPropertySet.Builder().build());
  }

  @Test
  void testWidthGetSet() {
    OptionalNumber width = OptionalNumber.get("4.5");
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withWidth(width).build();
    assertEquals(width, accessibilityPropertySet.getWidth());
  }

  @Test
  void testLitGetSet() {
    OptionalBoolean lit = OptionalBoolean.of(true);
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withLit(lit).build();
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
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withSurface(surface).build();
    assertEquals(surface, accessibilityPropertySet.getSurface());
  }

  @Test
  void testTactilePavingGetSet() {
    OptionalBoolean tactilePaving = OptionalBoolean.of(true);
    accessibilityPropertySet =
      new AccessibilityPropertySet.Builder().withTactilePaving(tactilePaving).build();
    assertEquals(tactilePaving, accessibilityPropertySet.getTactilePaving());
  }

  @Test
  void testSmoothnessGetSet() {
    OptionalEnum smoothness = optionalEnumOf("intermediate");
    accessibilityPropertySet =
      new AccessibilityPropertySet.Builder().withSmoothness(smoothness).build();
    assertEquals(smoothness, accessibilityPropertySet.getSmoothness());
  }

  @Test
  void testHighwayGetSet() {
    OptionalEnum highway = optionalEnumOf("footway");
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withHighway(highway).build();
    assertEquals(highway, accessibilityPropertySet.getHighway());
  }

  @Test
  void testFootwayGetSet() {
    OptionalEnum footway = optionalEnumOf("crossing");
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withFootway(footway).build();
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
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withIncline(incline).build();
    assertEquals(incline, accessibilityPropertySet.getIncline());
  }

  @Test
  void testTravHTrtGetSet() {
    OptionalDouble travHTrt = OptionalDouble.of(0.25);
    accessibilityPropertySet =
      new AccessibilityPropertySet.Builder().withTravHTrt(travHTrt).build();
    assertEquals(travHTrt, accessibilityPropertySet.getTravHTrt());
  }
}
