package org.opentripplanner.graph_builder.module.osm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.opentripplanner.openstreetmap.model.OSMBEVEtat;
import org.opentripplanner.openstreetmap.model.OSMFootway;
import org.opentripplanner.openstreetmap.model.OSMHighway;
import org.opentripplanner.openstreetmap.model.OSMSmoothness;
import org.opentripplanner.openstreetmap.model.OSMSurface;
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
    @NotNull OptionalEnum<OSMSurface> surface,
    @NotNull OptionalBoolean tactilePaving,
    @NotNull OptionalEnum<OSMSmoothness> smoothness,
    @NotNull OptionalEnum<OSMHighway> highway,
    @NotNull OptionalEnum<OSMFootway> footway,
    @NotNull OptionalEnumAndDouble incline,
    @NotNull OptionalNumber ressautMax,
    @NotNull OptionalNumber ressautMin,
    @NotNull OptionalEnum<OSMBEVEtat> bevEtat,
    @NotNull OptionalBoolean bevCtrast
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
    this.properties.put("wgt:ressaut_min", ressautMin);
    this.properties.put("wgt:bev_etat", bevEtat);
    this.properties.put("wgt:bev_ctrast", bevCtrast);
  }

  public OptionalNumber getWidth() {
    return (OptionalNumber) properties.getOrDefault("width", OptionalNumber.empty());
  }

  public OptionalBoolean getLit() {
    return (OptionalBoolean) properties.getOrDefault("lit", OptionalBoolean.empty());
  }

  public OptionalEnum<OSMSurface> getSurface() {
    return (OptionalEnum<OSMSurface>) properties.getOrDefault("surface", OptionalEnum.empty());
  }

  public OptionalBoolean getTactilePaving() {
    return (OptionalBoolean) properties.getOrDefault("tactile_paving", OptionalBoolean.empty());
  }

  public OptionalEnum<OSMSmoothness> getSmoothness() {
    return (OptionalEnum<OSMSmoothness>) properties.getOrDefault("smoothness", OptionalEnum.empty());
  }

  public OptionalEnum<OSMHighway> getHighway() {
    return (OptionalEnum<OSMHighway>) properties.getOrDefault("highway", OptionalEnum.empty());
  }

  public OptionalEnum<OSMFootway> getFootway() {
    return (OptionalEnum<OSMFootway>) properties.getOrDefault("footway", OptionalEnum.empty());
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

  public OptionalNumber getRessautMin() {
    return (OptionalNumber) properties.getOrDefault("wgt:ressaut_min", OptionalNumber.empty());
  }

  public OptionalEnum<OSMBEVEtat> getBevEtat() {
    return (OptionalEnum<OSMBEVEtat>) properties.getOrDefault("wgt:bev_etat", OptionalEnum.empty());
  }

  public OptionalBoolean getBevCtrast() {
    return (OptionalBoolean) properties.getOrDefault("wgt:bev_ctrast", OptionalBoolean.empty());
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
    private OptionalEnum<OSMSurface> surface = OptionalEnum.empty();
    private OptionalBoolean tactilePaving = OptionalBoolean.empty();
    private OptionalEnum<OSMSmoothness> smoothness = OptionalEnum.empty();
    private OptionalEnum<OSMHighway> highway = OptionalEnum.empty();
    private OptionalEnum<OSMFootway> footway = OptionalEnum.empty();
    private OptionalEnumAndDouble incline = OptionalEnumAndDouble.empty();
    private OptionalNumber ressautMax = OptionalNumber.empty();
    private OptionalNumber ressautMin = OptionalNumber.empty();
    private OptionalEnum<OSMBEVEtat> bevEtat = OptionalEnum.empty();
    private OptionalBoolean bevCtrast = OptionalBoolean.empty();

    public Builder withWidth(OptionalNumber width) {
      this.width = width;
      return this;
    }

    public Builder withLit(OptionalBoolean lit) {
      this.lit = lit;
      return this;
    }

    public Builder withSurface(OptionalEnum<OSMSurface> surface) {
      this.surface = surface;
      return this;
    }

    public Builder withTactilePaving(OptionalBoolean tactilePaving) {
      this.tactilePaving = tactilePaving;
      return this;
    }

    public Builder withSmoothness(OptionalEnum<OSMSmoothness> smoothness) {
      this.smoothness = smoothness;
      return this;
    }

    public Builder withHighway(OptionalEnum<OSMHighway> highway) {
      this.highway = highway;
      return this;
    }

    public Builder withFootway(OptionalEnum<OSMFootway> footway) {
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

    public Builder withRessautMin(OptionalNumber ressautMin) {
      this.ressautMin = ressautMin;
      return this;
    }

    public Builder withBevEtat(OptionalEnum<OSMBEVEtat> bevEtat) {
      this.bevEtat = bevEtat;
      return this;
    }

    public Builder withBevCtrast(OptionalBoolean bevCtrast) {
      this.bevCtrast = bevCtrast;
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
        ressautMax,
        ressautMin,
        bevEtat,
        bevCtrast
      );
    }
  }
}
