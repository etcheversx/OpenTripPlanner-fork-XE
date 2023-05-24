package org.opentripplanner.common.optional;

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
    supportedEnums.addAll(Arrays.stream(TestEnum.values()).toList());
  }

  @Test
  void testDoNotThrow() {
    for (Enum<?> e : supportedEnums) {
      assertDoesNotThrow((Executable) () -> OptionalEnumAndDouble.get(e.name(), e.getClass()));
    }
    assertDoesNotThrow((Executable) () -> OptionalEnumAndDouble.get("5.1", TestEnum.class));
  }

  private OptionalEnumAndDouble createOptionalEnumAndDoubleOf(Enum<?> e) {
    try {
      return OptionalEnumAndDouble.get(e.name(), e.getClass());
    } catch (Exception exc) {
      return OptionalEnumAndDouble.empty();
    }
  }

  private OptionalEnumAndDouble createOptionalEnumAndDoubleOf(double d) {
    try {
      return OptionalEnumAndDouble.get(Double.toString(d), TestEnum.class);
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
      assertEquals(
        createOptionalEnumAndDoubleOf(TestEnum.up),
        OptionalEnumAndDouble.get("up", TestEnum.class)
      );
      assertEquals(
        createOptionalEnumAndDoubleOf(TestEnum.down),
        OptionalEnumAndDouble.get("down", TestEnum.class)
      );
      assertEquals(
        createOptionalEnumAndDoubleOf(-2.33),
        OptionalEnumAndDouble.get("-2.33", TestEnum.class)
      );
    } catch (Exception exc) {
      fail("Get failed with: " + exc.getMessage());
    }

    try {
      OptionalEnumAndDouble.get("foo", TestEnum.class);
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
  void testGetAsObject() {
    for (Enum<?> enumerate : supportedEnums) {
      assertSame(enumerate, createOptionalEnumAndDoubleOf(enumerate).getAsTyped());
    }
    assertEquals(-12.3, createOptionalEnumAndDoubleOf(-12.3).getAsTyped());

    try {
      OptionalEnumAndDouble.empty().getAsTyped();
      fail("getAsEnum should faild with NoSuchElementException");
    } catch (NoSuchElementException exc) {
      NoSuchElementException expectedException = new NoSuchElementException("No value present");
      assertEquals(expectedException.getMessage(), exc.getMessage());
    }
  }
}
