package org.opentripplanner.openstreetmap.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
  /*

  @Test
  void testIsEmpty() {
    assertTrue(OptionalEnum.empty().isEmpty());
    for (Enum<?> enumerate : supportedEnums) {
      assertFalse(OptionalEnum.of(enumerate).isEmpty());
    }
  }

  @Test
  void testIsPresent() {
    assertFalse(OptionalEnum.empty().isPresent());
    for (Enum<?> enumerate : supportedEnums) {
      assertTrue(OptionalEnum.of(enumerate).isPresent());
    }
  }

  @Test
  void testGetAsEnum() {
    for (Enum<?> enumerate : supportedEnums) {
      assertSame(enumerate, OptionalEnum.of(enumerate).getAsEnum());
    }

    try {
      OptionalEnum.empty().getAsEnum();
    } catch (NoSuchElementException exc) {
      NoSuchElementException expectedException = new NoSuchElementException("No value present");
      assertEquals(expectedException.getMessage(), exc.getMessage());
      return;
    }
    fail("getAsBoolean should faild with NoSuchElementException");
  }

  @Test
  void testParseValues() {
    assertEquals(0, OptionalEnum.parseValues(null).size());

    assertEquals(0, OptionalEnum.parseValues("").size());

    ArrayList<OptionalEnum> parsedValues;
    ArrayList<OptionalEnum> expectedValues;

    parsedValues = OptionalEnum.parseValues("sand");
    expectedValues = new ArrayList<>();
    expectedValues.add(OptionalEnum.of(OSMSurface.sand));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;grass");
    expectedValues = new ArrayList<>();
    expectedValues.add(OptionalEnum.of(OSMSurface.sand));
    expectedValues.add(OptionalEnum.of(OSMSurface.grass));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;foo;grass");
    expectedValues = new ArrayList<>();
    expectedValues.add(OptionalEnum.of(OSMSurface.sand));
    expectedValues.add(OptionalEnum.of(OSMSurface.grass));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;sand");
    expectedValues = new ArrayList<>();
    expectedValues.add(OptionalEnum.of(OSMSurface.sand));
    assertEquals(expectedValues, parsedValues);
  }
  */
}
