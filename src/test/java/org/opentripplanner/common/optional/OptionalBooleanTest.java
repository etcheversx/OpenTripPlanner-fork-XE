package org.opentripplanner.common.optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class OptionalBooleanTest {

  @Test
  void testOptionalBooleanCreation() {
    OptionalBoolean optionalBoolean;

    optionalBoolean = OptionalBoolean.of(true);
    assertEquals(OptionalBoolean.yes(), optionalBoolean);

    optionalBoolean = OptionalBoolean.of(false);
    assertEquals(OptionalBoolean.no(), optionalBoolean);
  }

  @Test
  void testIsEmpty() {
    assertTrue(OptionalBoolean.empty().isEmpty());
    assertFalse(OptionalBoolean.yes().isEmpty());
    assertFalse(OptionalBoolean.no().isEmpty());
  }

  @Test
  void testIsPresent() {
    assertFalse(OptionalBoolean.empty().isPresent());
    assertTrue(OptionalBoolean.yes().isPresent());
    assertTrue(OptionalBoolean.no().isPresent());
  }

  @Test
  void testGetAsBoolean() {
    assertTrue(OptionalBoolean.yes().getAsTyped());
    assertFalse(OptionalBoolean.no().getAsTyped());

    try {
      OptionalBoolean.empty().getAsTyped();
    } catch (NoSuchElementException exc) {
      NoSuchElementException expectedException = new NoSuchElementException("No value present");
      assertEquals(expectedException.getMessage(), exc.getMessage());
      return;
    }
    fail("getAsBoolean should fail with NoSuchElementException");
  }
}
