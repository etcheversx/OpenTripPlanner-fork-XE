package org.opentripplanner.routing.api.request.preference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.opentripplanner.routing.api.request.preference.ImmutablePreferencesAsserts.assertEqualsAndHashCode;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.opentripplanner.openstreetmap.model.OSMSmoothness;
import org.opentripplanner.openstreetmap.model.OSMSurface;

class WalkPreferencesTest {

  private static final double SPEED = 1.7111;
  private static final double EXPECTED_SPEED = 1.71;
  private static final double RELUCTANCE = 2.51;
  private static final double EXPECTED_RELUCTANCE = 2.5;
  private static final int BOARD_COST = 301;
  private static final double STAIRS_RELUCTANCE = 3.011;
  private static final double EXPECTED_STAIRS_RELUCTANCE = 3.0;
  private static final double STAIRS_TIME_FACTOR = 1.3111;
  private static final double EXPECTED_STAIRS_TIME_FACTOR = 1.31;
  private static final double SAFETY_FACTOR = 0.5111;
  private static final double EXPECTED_SAFETY_FACTOR = 0.51;
  private static final double MINIMAL_WIDTH = 0.85;
  private static final boolean LIGHT_REQUIRED = true;
  private static final OSMSurface[] _RELUCTED_SURFACES = { OSMSurface.sand, OSMSurface.grass };
  private static final Collection<OSMSurface> RELUCTED_SURFACES = Arrays.asList(_RELUCTED_SURFACES);
  private static final boolean TACTILE_PAVING = true;
  private static final OSMSmoothness RELUCTED_SMOOTHNESS = OSMSmoothness.intermediate;
  private static final double MAXIMAL_INCLINE = 1.25;
  private static final double MAXIMAL_TRAV_H_TRT = 0.17;
  private static final AccessibilityProfile ACCESSIBILITY_PROFILE = AccessibilityProfile.PAM;

  private final WalkPreferences subject = WalkPreferences
    .of()
    .withSpeed(SPEED)
    .withReluctance(RELUCTANCE)
    .withBoardCost(BOARD_COST)
    .withStairsReluctance(STAIRS_RELUCTANCE)
    .withStairsTimeFactor(STAIRS_TIME_FACTOR)
    .withSafetyFactor(SAFETY_FACTOR)
    .withMinimalWidth(MINIMAL_WIDTH)
    .withLightRequired(LIGHT_REQUIRED)
    .withReluctedSurfaces(RELUCTED_SURFACES)
    .withTactilePaving(TACTILE_PAVING)
    .withReluctedSmoothness(RELUCTED_SMOOTHNESS)
    .withMaximalIncline(MAXIMAL_INCLINE)
    .withMaximalTravHTrt(MAXIMAL_TRAV_H_TRT)
    .withAccessibilityProfile(ACCESSIBILITY_PROFILE)
    .build();

  @Test
  void speed() {
    assertEquals(EXPECTED_SPEED, subject.speed());
  }

  @Test
  void reluctance() {
    assertEquals(EXPECTED_RELUCTANCE, subject.reluctance());
  }

  @Test
  void boardCost() {
    assertEquals(BOARD_COST, subject.boardCost());
  }

  @Test
  void stairsReluctance() {
    assertEquals(EXPECTED_STAIRS_RELUCTANCE, subject.stairsReluctance());
  }

  @Test
  void stairsTimeFactor() {
    assertEquals(EXPECTED_STAIRS_TIME_FACTOR, subject.stairsTimeFactor());
  }

  @Test
  void safetyFactor() {
    assertEquals(EXPECTED_SAFETY_FACTOR, subject.safetyFactor());
  }

  @Test
  void testEqualsAndHAshCode() {
    // Return same object if no value is set
    assertSame(subject, subject.copyOf().build());
    assertSame(TransitPreferences.DEFAULT, TransitPreferences.of().build());

    // By changing the speed back and forth we force the builder to create a new instance
    var other = subject.copyOf().withSpeed(10.0).build();
    var copy = other.copyOf().withSpeed(SPEED).build();
    assertEqualsAndHashCode(StreetPreferences.DEFAULT, subject, other, copy);
  }

  @Test
  void testToString() {
    assertEquals("WalkPreferences{}", WalkPreferences.DEFAULT.toString());
    assertEquals(
      "WalkPreferences{speed: 1.71, reluctance: 2.5, boardCost: 301, stairsReluctance: 3.0, stairsTimeFactor: 1.31, safetyFactor: 0.51, minimalWidth: 0.85, lightRequired, reluctedSurfaces: [sand, grass], tactilePaving, reluctedSmoothness: 'intermediate', maximalIncline: 1.25, maximalTravHTrt: 0.17, accessibilityProfile: 'PAM'}",
      subject.toString()
    );
  }

  void assertNotTheSame(Consumer<WalkPreferences.Builder> body) {
    var copy = subject.copyOf();
    body.accept(copy);
    WalkPreferences walk = copy.build();
    assertNotEquals(subject, walk);
  }

  @Test
  void minimalWidth() {
    assertEquals(MINIMAL_WIDTH, subject.minimalWidth());
  }

  @Test
  void lightRequired() {
    assertEquals(LIGHT_REQUIRED, subject.lightRequired());
  }

  @Test
  void reluctedSurfaces() {
    assertEquals(RELUCTED_SURFACES, subject.reluctedSurfaces());
  }

  @Test
  void tactilePaving() {
    assertEquals(TACTILE_PAVING, subject.tactilePaving());
  }

  @Test
  void reluctedSmoothness() {
    assertEquals(RELUCTED_SMOOTHNESS, subject.reluctedSmoothness());
  }

  @Test
  void maximalIncline() {
    assertEquals(MAXIMAL_INCLINE, subject.maximalIncline());
  }

  @Test
  void maximalTravHTrt() {
    assertEquals(MAXIMAL_TRAV_H_TRT, subject.maximalTravHTrt());
  }

  @Test
  void accessibilityProfile() {
    assertEquals(ACCESSIBILITY_PROFILE, subject.accessibilityProfile());
  }
}
