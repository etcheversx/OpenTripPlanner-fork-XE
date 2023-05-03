package org.opentripplanner.openstreetmap.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class OptionalEnumAndDoubleTest {

  private List<Enum<?>> supportedEnums;

  @BeforeEach
  void setup() {
    supportedEnums = new ArrayList<>();
    supportedEnums.addAll(Arrays.stream(OSMIncline.values()).toList());
  }

  @Test
  void testDoNotThrow() {
    for (Enum<?> e : supportedEnums) {
      assertDoesNotThrow((Executable) () -> OptionalEnumAndDouble.get(e.name()));
    }
    assertDoesNotThrow((Executable) () -> OptionalEnumAndDouble.get("5.1"));
  }

  private OptionalEnumAndDouble createOptionalEnumAndDoubleOf(Enum<?> e) {
    try {
      return OptionalEnumAndDouble.get(e.name());
    } catch (Exception exc) {
      return OptionalEnumAndDouble.empty();
    }
  }

  private OptionalEnumAndDouble createOptionalEnumAndDoubleOf(double d) {
    try {
      return OptionalEnumAndDouble.get(Double.toString(d));
    } catch (Exception exc) {
      return OptionalEnumAndDouble.empty();
    }
  }

  @Test
  void testOptionalEnumAndDoubleCreation() {
    OptionalEnumAndDouble expectedValue;
    OptionalEnumAndDouble optionalEnumAndDouble;

    for (Enum<?> enumerate : supportedEnums) {
      optionalEnumAndDouble = createOptionalEnumAndDoubleOf(enumerate);
      expectedValue = createOptionalEnumAndDoubleOf(enumerate);

      assertEquals(expectedValue, optionalEnumAndDouble);
    }

    optionalEnumAndDouble = createOptionalEnumAndDoubleOf(0.31);
    expectedValue = createOptionalEnumAndDoubleOf(0.31);

    assertEquals(expectedValue, optionalEnumAndDouble);
  }

  @Test
  void testGet() {
    try {
      assertEquals(createOptionalEnumAndDoubleOf(OSMIncline.up), OptionalEnumAndDouble.get("up"));
      assertEquals(
        createOptionalEnumAndDoubleOf(OSMIncline.down),
        OptionalEnumAndDouble.get("down")
      );
      assertEquals(createOptionalEnumAndDoubleOf(-2.33), OptionalEnumAndDouble.get("-2.33"));
    } catch (Exception exc) {
      fail("Get failed with: " + exc.getMessage());
    }

    try {
      OptionalEnumAndDouble.get("foo");
      fail("Get should fail on foo value");
    } catch (Exception exc) {
      assertEquals("For input string: \"foo\"", exc.getMessage());
    }
  }

  @Test
  void testIsEmpty() {
    assertTrue(OptionalEnumAndDouble.empty().isEmpty());
    for (Enum<?> enumerate : supportedEnums) {
      assertFalse(createOptionalEnumAndDoubleOf(enumerate).isEmpty());
    }
  }

  @Test
  void testIsPresent() {
    assertFalse(OptionalEnumAndDouble.empty().isPresent());
    for (Enum<?> enumerate : supportedEnums) {
      assertTrue(createOptionalEnumAndDoubleOf(enumerate).isPresent());
    }
  }

  @Test
  void testGetAsEnum() {
    for (Enum<?> enumerate : supportedEnums) {
      assertSame(enumerate, createOptionalEnumAndDoubleOf(enumerate).getAsEnum());
    }

    try {
      OptionalEnumAndDouble.empty().getAsEnum();
      fail("getAsEnum should faild with NoSuchElementException");
    } catch (NoSuchElementException exc) {
      NoSuchElementException expectedException = new NoSuchElementException("No value present");
      assertEquals(expectedException.getMessage(), exc.getMessage());
    }
    try {
      createOptionalEnumAndDoubleOf(-12.3).getAsEnum();
      fail("getAsEnum should faild with NoSuchElementException");
    } catch (NoSuchElementException exc) {
      NoSuchElementException expectedException = new NoSuchElementException("No value present");
      assertEquals(expectedException.getMessage(), exc.getMessage());
    }
  }
}
