package org.opentripplanner.graph_builder.module.osm;

import java.util.OptionalDouble;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;

public class AccessibilityPropertySet {
  public OptionalDouble getWidth() {
    return width;
  }

  public OptionalBoolean getLit() {
    return lit;
  }

  public OptionalEnum getSurface() {
    return surface;
  }

  final static AccessibilityPropertySet DEFAULT = new AccessibilityPropertySet();

  private OptionalDouble width;
  private OptionalBoolean lit;
  private OptionalEnum surface;

  AccessibilityPropertySet() {
    this.width = OptionalDouble.empty();
    this.lit = OptionalBoolean.empty();
    this.surface = OptionalEnum.empty();
  }

  private AccessibilityPropertySet(Builder builder) {
    this.width = builder.width();
    this.lit = builder.lit();
    this.surface = builder.surface();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessibilityPropertySet that = (AccessibilityPropertySet) o;
    return width.equals(that.width) && lit.equals(that.lit) && surface.equals(that.surface);
  }

  public static class Builder {
    private OptionalDouble width = OptionalDouble.empty();
    private OptionalBoolean lit = OptionalBoolean.empty();
    private OptionalEnum surface = OptionalEnum.empty();

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
      return new AccessibilityPropertySet(this);
    }
  }
}
