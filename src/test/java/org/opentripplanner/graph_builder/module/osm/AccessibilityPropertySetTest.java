package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.opentripplanner.openstreetmap.model.OSMBEVEtat;
import org.opentripplanner.openstreetmap.model.OSMFootway;
import org.opentripplanner.openstreetmap.model.OSMHighway;
import org.opentripplanner.openstreetmap.model.OSMIncline;
import org.opentripplanner.openstreetmap.model.OSMSmoothness;
import org.opentripplanner.openstreetmap.model.OSMSurface;
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
  void testPropertyKeys() {
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().build();

    String[] expected = {
      "width",
      "lit",
      "surface",
      "tactile_paving",
      "smoothness",
      "highway",
      "footway",
      "incline",
      "wgt:ressaut_max",
      "wgt:ressaut_min",
      "wgt:bev_etat",
      "wgt:bev_ctrast",
    };
    String[] propertyKeys = accessibilityPropertySet.propertyKeys();
    assertArrayEquals(
      Arrays.stream(expected).sorted().toArray(),
      Arrays.stream(propertyKeys).sorted().toArray()
    );
  }

  @Test
  void testGetProperty() {
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().build();

    assertNull(accessibilityPropertySet.getProperty("foo"));

    OptionalNumber width = OptionalNumber.get("1.79");
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withWidth(width).build();

    assertEquals(width, accessibilityPropertySet.getProperty("width"));
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

  private static <E extends Enum<E>> OptionalEnum<E> optionalEnumOf(
    String value,
    Class<E> enumClass
  ) {
    try {
      return OptionalEnum.get(value, enumClass);
    } catch (Exception exc) {
      return OptionalEnum.empty();
    }
  }

  @Test
  void testSurfaceGetSet() {
    OptionalEnum<OSMSurface> surface = optionalEnumOf("paved", OSMSurface.class);
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
    OptionalEnum<OSMSmoothness> smoothness = optionalEnumOf("intermediate", OSMSmoothness.class);
    accessibilityPropertySet =
      new AccessibilityPropertySet.Builder().withSmoothness(smoothness).build();
    assertEquals(smoothness, accessibilityPropertySet.getSmoothness());
  }

  @Test
  void testHighwayGetSet() {
    OptionalEnum<OSMHighway> highway = optionalEnumOf("footway", OSMHighway.class);
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withHighway(highway).build();
    assertEquals(highway, accessibilityPropertySet.getHighway());
  }

  @Test
  void testFootwayGetSet() {
    OptionalEnum<OSMFootway> footway = optionalEnumOf("crossing", OSMFootway.class);
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withFootway(footway).build();
    assertEquals(footway, accessibilityPropertySet.getFootway());
  }

  @Test
  void testInclineGetSet() {
    OptionalEnumAndDouble incline = OptionalEnumAndDouble.empty();
    try {
      incline = OptionalEnumAndDouble.get("2.1", OSMIncline.class);
    } catch (Exception exc) {
      fail("OptionalEnumAndDouble creation should not fail");
    }
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withIncline(incline).build();
    assertEquals(incline, accessibilityPropertySet.getIncline());
  }

  @Test
  void testRessautMaxGetSet() {
    OptionalNumber ressautMax = OptionalNumber.get("0.25");
    accessibilityPropertySet =
      new AccessibilityPropertySet.Builder().withRessautMax(ressautMax).build();
    assertEquals(ressautMax, accessibilityPropertySet.getRessautMax());
  }

  @Test
  void testRessautMinGetSet() {
    OptionalNumber ressautMin = OptionalNumber.get("0.12");
    accessibilityPropertySet =
      new AccessibilityPropertySet.Builder().withRessautMin(ressautMin).build();
    assertEquals(ressautMin, accessibilityPropertySet.getRessautMin());
  }

  @Test
  void testBevEtatGetSet() {
    OptionalEnum<OSMBEVEtat> bevEtat = optionalEnumOf("bad", OSMBEVEtat.class);
    accessibilityPropertySet = new AccessibilityPropertySet.Builder().withBevEtat(bevEtat).build();
    assertEquals(bevEtat, accessibilityPropertySet.getBevEtat());
  }

  @Test
  void testBevCtrastGetSet() {
    OptionalBoolean bevCtrast = OptionalBoolean.no();
    accessibilityPropertySet =
      new AccessibilityPropertySet.Builder().withBevCtrast(bevCtrast).build();
    assertEquals(bevCtrast, accessibilityPropertySet.getBevCtrast());
  }
}
