package org.opentripplanner.graph_builder.module.osm;

import java.io.Serializable;
import java.util.OptionalDouble;
import javax.validation.constraints.NotNull;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;
import org.opentripplanner.openstreetmap.model.OptionalNumber;

public class AccessibilityPropertySet implements Serializable {

  private final OptionalNumber width;
  private final OptionalBoolean lit;
  private final OptionalEnum surface;
  private final OptionalBoolean tactilePaving;
  private final OptionalEnum smoothness;
  private final OptionalEnum highway;
  private final OptionalEnum footway;
  private final OptionalEnumAndDouble incline;
  private final OptionalDouble travHTrt;

  private AccessibilityPropertySet(
    @NotNull OptionalNumber width,
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

  public OptionalNumber getWidth() {
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

  public static class Builder {

    private OptionalNumber width = OptionalNumber.empty();
    private OptionalBoolean lit = OptionalBoolean.empty();
    private OptionalEnum surface = OptionalEnum.empty();
    private OptionalBoolean tactilePaving = OptionalBoolean.empty();
    private OptionalEnum smoothness = OptionalEnum.empty();
    private OptionalEnum highway = OptionalEnum.empty();
    private OptionalEnum footway = OptionalEnum.empty();
    private OptionalEnumAndDouble incline = OptionalEnumAndDouble.empty();
    private OptionalDouble travHTrt = OptionalDouble.empty();

    public Builder withWidth(OptionalNumber width) {
      this.width = width;
      return this;
    }

    public Builder withLit(OptionalBoolean lit) {
      this.lit = lit;
      return this;
    }

    public Builder withSurface(OptionalEnum surface) {
      this.surface = surface;
      return this;
    }

    public Builder withTactilePaving(OptionalBoolean tactilePaving) {
      this.tactilePaving = tactilePaving;
      return this;
    }

    public Builder withSmoothness(OptionalEnum smoothness) {
      this.smoothness = smoothness;
      return this;
    }

    public Builder withHighway(OptionalEnum highway) {
      this.highway = highway;
      return this;
    }

    public Builder withFootway(OptionalEnum footway) {
      this.footway = footway;
      return this;
    }

    public Builder withIncline(OptionalEnumAndDouble incline) {
      this.incline = incline;
      return this;
    }

    public Builder withTravHTrt(OptionalDouble travHTrt) {
      this.travHTrt = travHTrt;
      return this;
    }

    public AccessibilityPropertySet build() {
      return new AccessibilityPropertySet(
        width,
        lit,
        surface,
        tactilePaving,
        smoothness,
        highway,
        footway,
        incline,
        travHTrt
      );
    }
  }
}
