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
import javax.swing.text.html.Option;
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

  @Test
  void testOptionalEnumsCreation() {
    for (Enum<?> e : supportedEnums) {
      assertDoesNotThrow((Executable) () -> OptionalEnum.of(e));
    }
  }

  @Test
  void testOptionalEnumCreation() {
    OptionalEnum expectedOptionalEnum;
    OptionalEnum optionalEnum;

    for (Enum<?> enumerate : supportedEnums) {
      optionalEnum = OptionalEnum.of(enumerate);
      expectedOptionalEnum = OptionalEnum.of(enumerate);

      assertSame(expectedOptionalEnum, optionalEnum);
    }
  }

  @Test
  void testGet() {
    try {
      assertSame(OptionalEnum.of(OSMSurface.paved), OptionalEnum.get("paved"));
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
}
