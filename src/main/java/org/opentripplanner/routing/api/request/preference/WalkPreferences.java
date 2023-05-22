package org.opentripplanner.routing.api.request.preference;

import static org.opentripplanner.util.lang.DoubleUtils.doubleEquals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import javax.validation.constraints.NotNull;
import org.opentripplanner.openstreetmap.model.OSMSmoothness;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.routing.api.request.framework.Units;
import org.opentripplanner.util.lang.ToStringBuilder;

/**
 * The walk preferences contain all speed, reluctance, cost and factor preferences for walking
 * related to street and transit routing. The values are normalized(rounded) so the class can used
 * as a cache key.
 * <p>
 * See the configuration for documentation of each field.
 * <p>
 * THIS CLASS IS IMMUTABLE AND THREAD-SAFE.
 */
public final class WalkPreferences implements Serializable {

  public static final WalkPreferences DEFAULT = new WalkPreferences();

  private final double speed;
  private final double reluctance;
  private final int boardCost;
  private final double stairsReluctance;
  private final double stairsTimeFactor;
  private final double safetyFactor;
  private final double minimalWidth;
  private final boolean lightRequired;
  private final Collection<OSMSurface> reluctedSurfaces;
  private final boolean tactilePaving;
  private final OSMSmoothness reluctedSmoothness;
  private final double maximalIncline;
  private final double ressautMax;
  private final double ressautMin;
  private final boolean bevCtrast;
  private final AccessibilityProfile accessibilityProfile;

  private WalkPreferences() {
    this.speed = 1.33;
    this.reluctance = 2.0;
    this.boardCost = 60 * 10;
    this.stairsReluctance = 2.0;
    this.stairsTimeFactor = 3.0;
    this.safetyFactor = 1.0;
    this.minimalWidth = 0.0;
    this.lightRequired = false;
    this.reluctedSurfaces = new ArrayList<>();
    this.tactilePaving = false;
    this.reluctedSmoothness = OSMSmoothness.impassable;
    this.maximalIncline = Double.MAX_VALUE;
    this.ressautMax = Double.MAX_VALUE;
    this.ressautMin = 0.0;
    this.bevCtrast = false;
    this.accessibilityProfile = null;
  }

  private WalkPreferences(Builder builder) {
    this.speed = Units.speed(builder.speed);
    this.reluctance = Units.reluctance(builder.reluctance);
    this.boardCost = Units.cost(builder.boardCost);
    this.stairsReluctance = Units.reluctance(builder.stairsReluctance);
    this.stairsTimeFactor = Units.reluctance(builder.stairsTimeFactor);
    this.safetyFactor = Units.reluctance(builder.safetyFactor);
    this.minimalWidth = Units.length(builder.minimalWidth);
    this.lightRequired = builder.lightRequired;
    this.reluctedSurfaces = builder.reluctedSurfaces;
    this.tactilePaving = builder.tactilePaving;
    this.reluctedSmoothness = builder.reluctedSmoothness;
    this.maximalIncline = builder.maximalIncline;
    this.ressautMax = builder.ressautMax;
    this.ressautMin = builder.ressautMin;
    this.bevCtrast = builder.bevCtrast;
    this.accessibilityProfile = builder.accessibilityProfile;
  }

  public static Builder of() {
    return new Builder(DEFAULT);
  }

  public Builder copyOf() {
    return new Builder(this);
  }

  /**
   * Human walk speed along streets, in meters per second.
   * <p>
   * Default: 1.33 m/s ~ 3mph, <a href="http://en.wikipedia.org/wiki/Walking">avg. human walk
   * speed</a>
   */
  public double speed() {
    return speed;
  }

  /**
   * A multiplier for how bad walking is, compared to being in transit for equal lengths of time.
   * Empirically, values between 2 and 4 seem to correspond well to the concept of not wanting to
   * walk too much without asking for totally ridiculous itineraries, but this observation should in
   * no way be taken as scientific or definitive. Your mileage may vary. See
   * https://github.com/opentripplanner/OpenTripPlanner/issues/4090 for impact on performance with
   * high values. Default value: 2.0
   */
  public double reluctance() {
    return reluctance;
  }

  /**
   * This prevents unnecessary transfers by adding a cost for boarding a vehicle. This is in
   * addition to the cost of the transfer(walking) and waiting-time. It is also in addition to the
   * {@link TransferPreferences#cost()}.
   */
  public int boardCost() {
    return boardCost;
  }

  /** Used instead of walk reluctance for stairs */
  public double stairsReluctance() {
    return stairsReluctance;
  }

  public double stairsTimeFactor() {
    return stairsTimeFactor;
  }

  /**
   * Factor for how much the walk safety is considered in routing. Value should be between 0 and 1.
   * If the value is set to be 0, safety is ignored.
   */
  public double safetyFactor() {
    return safetyFactor;
  }

  /*
   * Minimal width for which an edge can be considered while routing with no extra reluctance.
   * If the value is set to 0.0, all width are considered to be acceptable.
   */
  public double minimalWidth() {
    return minimalWidth;
  }

  /*
   * Is light required on an edge without introducing extra reluctance while routing.
   * If the value is set to false, all edges considered to be acceptable.
   */
  public boolean lightRequired() {
    return lightRequired;
  }

  public Collection<OSMSurface> reluctedSurfaces() {
    return reluctedSurfaces;
  }

  public boolean tactilePaving() {
    return tactilePaving;
  }

  public OSMSmoothness reluctedSmoothness() {
    return reluctedSmoothness;
  }

  public double maximalIncline() {
    return maximalIncline;
  }

  public double ressautMax() {
    return ressautMax;
  }

  public double ressautMin() {
    return ressautMin;
  }

  public boolean bevCtrast() {
    return bevCtrast;
  }

  public AccessibilityProfile accessibilityProfile() {
    return accessibilityProfile;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WalkPreferences that = (WalkPreferences) o;
    return (
      doubleEquals(that.speed, speed) &&
      doubleEquals(that.reluctance, reluctance) &&
      boardCost == that.boardCost &&
      doubleEquals(that.stairsReluctance, stairsReluctance) &&
      doubleEquals(that.stairsTimeFactor, stairsTimeFactor) &&
      doubleEquals(that.safetyFactor, safetyFactor) &&
      doubleEquals(that.minimalWidth, minimalWidth) &&
      lightRequired == that.lightRequired &&
      reluctedSurfaces.equals(that.reluctedSurfaces) &&
      tactilePaving == that.tactilePaving &&
      reluctedSmoothness.equals(that.reluctedSmoothness) &&
      doubleEquals(that.maximalIncline, maximalIncline) &&
      doubleEquals(that.ressautMax, ressautMax) &&
      doubleEquals(that.ressautMin, ressautMin) &&
      bevCtrast == that.bevCtrast &&
      Objects.equals(accessibilityProfile, that.accessibilityProfile)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      speed,
      reluctance,
      boardCost,
      stairsReluctance,
      stairsTimeFactor,
      safetyFactor,
      minimalWidth
    );
  }

  @Override
  public String toString() {
    return ToStringBuilder
      .of(WalkPreferences.class)
      .addNum("speed", speed, DEFAULT.speed)
      .addNum("reluctance", reluctance, DEFAULT.reluctance)
      .addNum("boardCost", boardCost, DEFAULT.boardCost)
      .addNum("stairsReluctance", stairsReluctance, DEFAULT.stairsReluctance)
      .addNum("stairsTimeFactor", stairsTimeFactor, DEFAULT.stairsTimeFactor)
      .addNum("safetyFactor", safetyFactor, DEFAULT.safetyFactor)
      .addNum("minimalWidth", minimalWidth, DEFAULT.minimalWidth)
      .addBoolIfTrue("lightRequired", lightRequired)
      .addCol("reluctedSurfaces", reluctedSurfaces)
      .addBoolIfTrue("tactilePaving", tactilePaving)
      .addStr(
        "reluctedSmoothness",
        reluctedSmoothness.toString(),
        OSMSmoothness.impassable.toString()
      )
      .addNum("maximalIncline", maximalIncline, DEFAULT.maximalIncline)
      .addNum("ressautMax", ressautMax, DEFAULT.ressautMax)
      .addNum("ressautMin", ressautMin, DEFAULT.ressautMin)
      .addBoolIfTrue("bevCtrast", bevCtrast)
      .addObj("accessibilityProfile", accessibilityProfile, null)
      .toString();
  }

  public static class Builder {

    private final WalkPreferences original;
    private double speed;
    private double reluctance;
    private int boardCost;
    private double stairsReluctance;
    private double stairsTimeFactor;
    private double safetyFactor;
    private double minimalWidth = 0.0;
    private boolean lightRequired = false;
    private Collection<OSMSurface> reluctedSurfaces = new ArrayList<>();
    private boolean tactilePaving = false;
    private OSMSmoothness reluctedSmoothness = OSMSmoothness.impassable;
    private double maximalIncline = 0.0;
    private double ressautMax = 0.0;
    private double ressautMin = 0.0;
    private boolean bevCtrast = true;
    private AccessibilityProfile accessibilityProfile = null;

    public Builder(WalkPreferences original) {
      this.original = original;
      this.speed = original.speed;
      this.reluctance = original.reluctance;
      this.boardCost = original.boardCost;
      this.stairsReluctance = original.stairsReluctance;
      this.stairsTimeFactor = original.stairsTimeFactor;
      this.safetyFactor = original.safetyFactor;
      this.minimalWidth = original.minimalWidth;
      this.lightRequired = original.lightRequired;
      this.reluctedSurfaces = original.reluctedSurfaces;
      this.tactilePaving = original.tactilePaving;
      this.reluctedSmoothness = original.reluctedSmoothness;
      this.maximalIncline = original.maximalIncline;
      this.ressautMax = original.ressautMax;
      this.ressautMin = original.ressautMin;
      this.bevCtrast = original.bevCtrast;
      this.accessibilityProfile = original.accessibilityProfile;
    }

    public WalkPreferences original() {
      return original;
    }

    public double speed() {
      return speed;
    }

    public Builder withSpeed(double speed) {
      this.speed = speed;
      return this;
    }

    public double reluctance() {
      return reluctance;
    }

    public Builder withReluctance(double reluctance) {
      this.reluctance = reluctance;
      return this;
    }

    public int boardCost() {
      return boardCost;
    }

    public Builder withBoardCost(int boardCost) {
      this.boardCost = boardCost;
      return this;
    }

    public double stairsReluctance() {
      return stairsReluctance;
    }

    public Builder withStairsReluctance(double stairsReluctance) {
      this.stairsReluctance = stairsReluctance;
      return this;
    }

    public double stairsTimeFactor() {
      return stairsTimeFactor;
    }

    public Builder withStairsTimeFactor(double stairsTimeFactor) {
      this.stairsTimeFactor = stairsTimeFactor;
      return this;
    }

    public double safetyFactor() {
      return safetyFactor;
    }

    public Builder withSafetyFactor(double safetyFactor) {
      if (safetyFactor < 0) {
        this.safetyFactor = 0;
      } else if (safetyFactor > 1) {
        this.safetyFactor = 1;
      } else {
        this.safetyFactor = safetyFactor;
      }
      return this;
    }

    public Builder withMinimalWidth(double minimalWidth) {
      this.minimalWidth = minimalWidth;
      return this;
    }

    public Builder withLightRequired(boolean lightRequired) {
      this.lightRequired = lightRequired;
      return this;
    }

    public Builder withReluctedSurfaces(@NotNull Collection<OSMSurface> reluctedSurfaces) {
      this.reluctedSurfaces = reluctedSurfaces;
      return this;
    }

    public Builder withTactilePaving(boolean tactilePaving) {
      this.tactilePaving = tactilePaving;
      return this;
    }

    public Builder withReluctedSmoothness(@NotNull OSMSmoothness reluctedSmoothness) {
      this.reluctedSmoothness = reluctedSmoothness;
      return this;
    }

    public Builder withMaximalIncline(double maximalIncline) {
      this.maximalIncline = maximalIncline;
      return this;
    }

    public Builder withRessautMax(double ressautMax) {
      this.ressautMax = ressautMax;
      return this;
    }

    public Builder withRessautMin(double ressautMin) {
      this.ressautMin = ressautMin;
      return this;
    }

    public Builder withBevCtrast(boolean bevCtrast) {
      this.bevCtrast = bevCtrast;
      return this;
    }

    public Builder withAccessibilityProfile(AccessibilityProfile accessibilityProfile) {
      this.accessibilityProfile = accessibilityProfile;
      return this;
    }

    public Builder apply(Consumer<Builder> body) {
      body.accept(this);
      return this;
    }

    public WalkPreferences build() {
      var newObj = new WalkPreferences(this);
      return original.equals(newObj) ? original : newObj;
    }
  }
}
