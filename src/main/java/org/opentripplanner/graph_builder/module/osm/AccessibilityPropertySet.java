package org.opentripplanner.graph_builder.module.osm;

import java.io.Serializable;
import java.util.OptionalDouble;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;

public class AccessibilityPropertySet implements Serializable {

  private final OptionalDouble width;
  private final OptionalBoolean lit;
  private final OptionalEnum surface;

  public AccessibilityPropertySet(OptionalDouble width, OptionalBoolean lit, OptionalEnum surface) {
    this.width = width;
    this.lit = lit;
    this.surface = surface;
  }

  public OptionalDouble getWidth() {
    return width;
  }

  public OptionalBoolean getLit() {
    return lit;
  }

  public OptionalEnum getSurface() {
    return surface;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessibilityPropertySet that = (AccessibilityPropertySet) o;
    return width.equals(that.width) && lit.equals(that.lit) && surface.equals(that.surface);
  }
}
