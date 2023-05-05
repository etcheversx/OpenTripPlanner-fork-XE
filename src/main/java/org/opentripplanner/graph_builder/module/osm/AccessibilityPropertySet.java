package org.opentripplanner.graph_builder.module.osm;

import java.io.Serializable;
import java.util.OptionalDouble;
import javax.validation.constraints.NotNull;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;

public class AccessibilityPropertySet implements Serializable {

  private final OptionalDouble width;
  private final OptionalBoolean lit;
  private final OptionalEnum surface;
  private final OptionalBoolean tactilePaving;
  private final OptionalEnum smoothness;
  private final OptionalEnum highway;
  private final OptionalEnum footway;
  private final OptionalEnumAndDouble incline;
  private final OptionalDouble travHTrt;

  public AccessibilityPropertySet(
    @NotNull OptionalDouble width,
    @NotNull OptionalBoolean lit,
    @NotNull OptionalEnum surface,
    @NotNull OptionalBoolean tactilePaving,
    @NotNull OptionalEnum smoothness,
    @NotNull OptionalEnum highway,
    @NotNull OptionalEnum footway,
    @NotNull OptionalEnumAndDouble incline,
    @NotNull OptionalDouble travHTrt
  ) {
    this.width = width;
    this.lit = lit;
    this.surface = surface;
    this.tactilePaving = tactilePaving;
    this.smoothness = smoothness;
    this.highway = highway;
    this.footway = footway;
    this.incline = incline;
    this.travHTrt = travHTrt;
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

  public OptionalBoolean getTactilePaving() {
    return tactilePaving;
  }

  public OptionalEnum getSmoothness() {
    return smoothness;
  }

  public OptionalEnum getHighway() {
    return highway;
  }

  public OptionalEnum getFootway() {
    return footway;
  }

  public OptionalEnumAndDouble getIncline() {
    return incline;
  }

  public OptionalDouble getTravHTrt() {
    return travHTrt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessibilityPropertySet that = (AccessibilityPropertySet) o;
    return (
      width.equals(that.width) &&
      lit.equals(that.lit) &&
      surface.equals(that.surface) &&
      tactilePaving.equals(that.tactilePaving) &&
      smoothness.equals(that.smoothness) &&
      highway.equals(that.highway) &&
      footway.equals(that.footway)
    );
  }
}
