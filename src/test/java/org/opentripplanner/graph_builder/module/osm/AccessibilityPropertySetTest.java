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
      new AccessibilityPropertySetBuilder()
        .withWidth(OptionalDouble.empty())
        .withLit(OptionalBoolean.empty())
        .withSurface(OptionalEnum.empty())
        .build();
    assertEquals(accessibilityPropertySet, new AccessibilityPropertySetBuilder().build());
  }

  @Test
  void testWidthGetSet() {
    OptionalDouble width = OptionalDouble.of(4.5);
    accessibilityPropertySet = new AccessibilityPropertySetBuilder().withWidth(width).build();
    assertEquals(width, accessibilityPropertySet.getWidth());
  }

  @Test
  void testLitGetSet() {
    OptionalBoolean lit = OptionalBoolean.of(true);
    accessibilityPropertySet = new AccessibilityPropertySetBuilder().withLit(lit).build();
    assertEquals(lit, accessibilityPropertySet.getLit());
  }

  @Test
  void testSurfaceGetSet() {
    OptionalEnum surface = OptionalEnum.of(OSMSurface.paved);
    accessibilityPropertySet = new AccessibilityPropertySetBuilder().withSurface(surface).build();
    assertEquals(surface, accessibilityPropertySet.getSurface());
  }

  public static class AccessibilityPropertySetBuilder {

    private OptionalDouble width = OptionalDouble.empty();
    private OptionalBoolean lit = OptionalBoolean.empty();
    private OptionalEnum surface = OptionalEnum.empty();

    public AccessibilityPropertySetBuilder withWidth(OptionalDouble width) {
      this.width = width;
      return this;
    }

    public AccessibilityPropertySetBuilder withLit(OptionalBoolean lit) {
      this.lit = lit;
      return this;
    }

    public AccessibilityPropertySetBuilder withSurface(OptionalEnum surface) {
      this.surface = surface;
      return this;
    }

    private OptionalDouble width() {
      return this.width;
    }

    private OptionalBoolean lit() {
      return this.lit;
    }

    private OptionalEnum surface() {
      return this.surface;
    }

    AccessibilityPropertySet build() {
      return new AccessibilityPropertySet(this.width(), this.lit(), this.surface());
    }
  }
}
