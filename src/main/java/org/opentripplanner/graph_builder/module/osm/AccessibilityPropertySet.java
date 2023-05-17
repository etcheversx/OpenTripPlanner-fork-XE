package org.opentripplanner.graph_builder.module.osm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;
import org.opentripplanner.openstreetmap.model.OptionalNumber;
import org.opentripplanner.openstreetmap.model.OptionalValue;

public class AccessibilityPropertySet implements Serializable {

  private final Map<String, OptionalValue<?>> properties = new HashMap<>();

  private AccessibilityPropertySet(
    @NotNull OptionalNumber width,
    @NotNull OptionalBoolean lit,
    @NotNull OptionalEnum surface,
    @NotNull OptionalBoolean tactilePaving,
    @NotNull OptionalEnum smoothness,
    @NotNull OptionalEnum highway,
    @NotNull OptionalEnum footway,
    @NotNull OptionalEnumAndDouble incline,
    @NotNull OptionalNumber ressautMax
  ) {
    this.properties.put("width", width);
    this.properties.put("lit", lit);
    this.properties.put("surface", surface);
    this.properties.put("tactile_paving", tactilePaving);
    this.properties.put("smoothness", smoothness);
    this.properties.put("highway", highway);
    this.properties.put("footway", footway);
    this.properties.put("incline", incline);
    this.properties.put("wgt:ressaut_max", ressautMax);
  }

  public OptionalNumber getWidth() {
    return (OptionalNumber) properties.getOrDefault("width", OptionalNumber.empty());
  }

  public OptionalBoolean getLit() {
    return (OptionalBoolean) properties.getOrDefault("lit", OptionalBoolean.empty());
  }

  public OptionalEnum getSurface() {
    return (OptionalEnum) properties.getOrDefault("surface", OptionalEnum.empty());
  }

  public OptionalBoolean getTactilePaving() {
    return (OptionalBoolean) properties.getOrDefault("tactile_paving", OptionalBoolean.empty());
  }

  public OptionalEnum getSmoothness() {
    return (OptionalEnum) properties.getOrDefault("smoothness", OptionalEnum.empty());
  }

  public OptionalEnum getHighway() {
    return (OptionalEnum) properties.getOrDefault("highway", OptionalEnum.empty());
  }

  public OptionalEnum getFootway() {
    return (OptionalEnum) properties.getOrDefault("footway", OptionalEnum.empty());
  }

  public OptionalEnumAndDouble getIncline() {
    return (OptionalEnumAndDouble) properties.getOrDefault(
      "incline",
      OptionalEnumAndDouble.empty()
    );
  }

  public OptionalNumber getRessautMax() {
    return (OptionalNumber) properties.getOrDefault("wgt:ressaut_max", OptionalNumber.empty());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessibilityPropertySet that = (AccessibilityPropertySet) o;
    return properties.equals(that.properties);
  }

  public String[] propertyKeys() {
    String[] result = {};
    return properties.keySet().toArray(result);
  }

  public OptionalValue getProperty(String key) {
    return properties.get(key);
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
    private OptionalNumber ressautMax = OptionalNumber.empty();

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

    public Builder withRessautMax(OptionalNumber ressautMax) {
      this.ressautMax = ressautMax;
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
        ressautMax
      );
    }
  }
}
