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

  private static OptionalEnum optionalEnumOf(String value) {
    try {
      return OptionalEnum.get(value);
    } catch (Exception exc) {
      return OptionalEnum.empty();
    }
  }

  @Test
  void testOptionalEnumsCreation() {
    for (Enum<?> e : supportedEnums) {
      assertDoesNotThrow((Executable) () -> optionalEnumOf(e.name()));
    }
  }

  @Test
  void testOptionalEnumCreation() {
    OptionalEnum expectedOptionalEnum;
    OptionalEnum optionalEnum;

    for (Enum<?> enumerate : supportedEnums) {
      optionalEnum = optionalEnumOf(enumerate.name());
      expectedOptionalEnum = optionalEnumOf(enumerate.name());

      assertSame(expectedOptionalEnum, optionalEnum);
    }
  }

  @Test
  void testGet() {
    try {
      assertSame(optionalEnumOf("paved"), OptionalEnum.get("paved"));
    } catch (Exception exc) {
      fail("Get failed with: " + exc.getMessage());
    }

    try {
      OptionalEnum.get("foo");
      fail("Get should fail on foo value");
    } catch (Exception exc) {
      assertEquals("Invalid enum value foo", exc.getMessage());
    }
  }

  @Test
  void testIsEmpty() {
    assertTrue(OptionalEnum.empty().isEmpty());
    for (Enum<?> enumerate : supportedEnums) {
      assertFalse(optionalEnumOf(enumerate.name()).isEmpty());
    }
  }

  @Test
  void testIsPresent() {
    assertFalse(OptionalEnum.empty().isPresent());
    for (Enum<?> enumerate : supportedEnums) {
      assertTrue(optionalEnumOf(enumerate.name()).isPresent());
    }
  }

  @Test
  void testGetAsEnum() {
    for (Enum<?> enumerate : supportedEnums) {
      assertSame(enumerate, optionalEnumOf(enumerate.name()).getAsEnum());
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
    expectedValues.add(optionalEnumOf("sand"));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;grass");
    expectedValues = new ArrayList<>();
    expectedValues.add(optionalEnumOf("sand"));
    expectedValues.add(optionalEnumOf("grass"));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;foo;grass");
    expectedValues = new ArrayList<>();
    expectedValues.add(optionalEnumOf("sand"));
    expectedValues.add(optionalEnumOf("grass"));
    assertEquals(expectedValues, parsedValues);

    parsedValues = OptionalEnum.parseValues("sand;sand");
    expectedValues = new ArrayList<>();
    expectedValues.add(optionalEnumOf("sand"));
    assertEquals(expectedValues, parsedValues);
  }
}
