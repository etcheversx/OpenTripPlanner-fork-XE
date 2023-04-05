package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AccessibilityPropertySetTest {
  @Test
  void testDefault() {

    assertEquals(new AccessibilityPropertySet.Builder().build(), AccessibilityPropertySet.DEFAULT);
  }
}