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

class OptionalEnumTest {

  private List<Enum<?>> supportedEnums;

  @BeforeEach
  void setup() {
    supportedEnums = new ArrayList<>();
    supportedEnums.addAll(Arrays.stream(OSMSurface.values()).toList());
  }

  private static <E extends Enum<E>> OptionalEnum<?> optionalEnumOf(
    String value,
    Class<E> enumClass
  ) {
    try {
      return OptionalEnum.get(value, enumClass);
    } catch (Exception exc) {
      return OptionalEnum.empty();
    }
  }

  @Test
  void testOptionalEnumsCreation() {
    for (Enum<?> e : supportedEnums) {
      assertDoesNotThrow((Executable) () -> optionalEnumOf(e.name(), e.getClass()));
    }
  }

  @Test
  void testOptionalEnumCreation() {
    OptionalEnum expectedOptionalEnum;
    OptionalEnum optionalEnum;

    for (Enum<?> enumerate : supportedEnums) {
      optionalEnum = optionalEnumOf(enumerate.name(), enumerate.getClass());
      expectedOptionalEnum = optionalEnumOf(enumerate.name(), enumerate.getClass());

      assertEquals(expectedOptionalEnum, optionalEnum);
    }
  }

  @Test
  void testGet() {
    try {
      assertEquals(
        optionalEnumOf("paved", OSMSurface.class),
        OptionalEnum.get("paved", OSMSurface.class)
      );
    } catch (Exception exc) {
      fail("Get failed with: " + exc.getMessage());
    }

    try {
      OptionalEnum.get("foo", OSMSurface.class);
      fail("Get should fail on foo value");
    } catch (Exception exc) {
      assertEquals("Invalid enum value foo", exc.getMessage());
    }
  }

  @Test
  void testIsEmpty() {
    assertTrue(OptionalEnum.empty().isEmpty());
    for (Enum<?> enumerate : supportedEnums) {
      assertFalse(optionalEnumOf(enumerate.name(), enumerate.getClass()).isEmpty());
    }
  }

  @Test
  void testIsPresent() {
    assertFalse(OptionalEnum.empty().isPresent());
    for (Enum<?> enumerate : supportedEnums) {
      assertTrue(optionalEnumOf(enumerate.name(), enumerate.getClass()).isPresent());
    }
  }

  @Test
  void testGetAsEnum() {
    for (Enum<?> enumerate : supportedEnums) {
      assertSame(enumerate, optionalEnumOf(enumerate.name(), enumerate.getClass()).getAsTyped());
    }

    try {
      OptionalEnum.empty().getAsTyped();
    } catch (NoSuchElementException exc) {
      NoSuchElementException expectedException = new NoSuchElementException("No value present");
      assertEquals(expectedException.getMessage(), exc.getMessage());
      return;
    }
    fail("getAsBoolean should faild with NoSuchElementException");
  }

  @Test
  void testParseValues() {
    assertEquals(0, OptionalEnum.parseValues(null, OSMSurface.class).size());

    assertEquals(0, OptionalEnum.parseValues("", OSMSurface.class).size());

    ArrayList<OptionalEnum<OSMSurface>> parsedValues;
    ArrayList<OptionalEnum> expectedValues;

    parsedValues = OptionalEnum.parseValues("sand", OSMSurface.class);
    expectedValues = new ArrayList<>();
    expectedValues.add(optionalEnumOf("sand", OSMSurface.class));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;grass", OSMSurface.class);
    expectedValues = new ArrayList<>();
    expectedValues.add(optionalEnumOf("sand", OSMSurface.class));
    expectedValues.add(optionalEnumOf("grass", OSMSurface.class));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;foo;grass", OSMSurface.class);
    expectedValues = new ArrayList<>();
    expectedValues.add(optionalEnumOf("sand", OSMSurface.class));
    expectedValues.add(optionalEnumOf("grass", OSMSurface.class));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;sand", OSMSurface.class);
    expectedValues = new ArrayList<>();
    expectedValues.add(optionalEnumOf("sand", OSMSurface.class));
    assertEquals(expectedValues, parsedValues);
  }
}
