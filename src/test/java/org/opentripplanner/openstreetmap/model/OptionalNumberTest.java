package org.opentripplanner.openstreetmap.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class OptionalNumberTest {

  @Test
  void testDoNotThrow() {
    assertDoesNotThrow((Executable) () -> OptionalNumber.get("5.1"));
  }

  private OptionalNumber createOptionalNuberOf(double d) {
    try {
      return OptionalNumber.get(Double.toString(d));
    } catch (Exception exc) {
      return OptionalNumber.empty();
    }
  }

  @Test
  void testOptionalNumberCreation() {
    OptionalNumber expectedValue;
    OptionalNumber optionalNumber;

    optionalNumber = createOptionalNuberOf(0.31);
    expectedValue = createOptionalNuberOf(0.31);

    assertEquals(expectedValue, optionalNumber);
  }

  @Test
  void testGet() {
    try {
      assertEquals(createOptionalNuberOf(-2.33), OptionalNumber.get("-2.33"));
    } catch (Exception exc) {
      fail("Get failed with: " + exc.getMessage());
    }

    try {
      OptionalNumber.get("foo");
      fail("Get should fail on foo value");
    } catch (Exception exc) {
      assertEquals("For input string: \"foo\"", exc.getMessage());
    }
  }

  @Test
  void testIsEmpty() {
    assertTrue(OptionalNumber.empty().isEmpty());
  }

  @Test
  void testIsPresent() {
    assertFalse(OptionalNumber.empty().isPresent());
  }

  @Test
  void testGetAsObject() {
    assertEquals(-12.3, createOptionalNuberOf(-12.3).getAsTyped());

    try {
      OptionalNumber.empty().getAsTyped();
      fail("getAsTyped should fail with NoSuchElementException");
    } catch (NoSuchElementException exc) {
      NoSuchElementException expectedException = new NoSuchElementException("No value present");
      assertEquals(expectedException.getMessage(), exc.getMessage());
    }
  }
}
