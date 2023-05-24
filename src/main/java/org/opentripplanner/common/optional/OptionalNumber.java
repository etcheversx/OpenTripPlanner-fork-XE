package org.opentripplanner.common.optional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalDouble;
import javax.validation.constraints.NotNull;

public class OptionalNumber implements OptionalValue {

  private static final OptionalNumber empty = new OptionalNumber();
  private final OptionalDouble value;

  private OptionalNumber() {
    this.value = OptionalDouble.empty();
  }

  private OptionalNumber(@NotNull OptionalDouble value) {
    this.value = value;
  }

  public static OptionalNumber get(String value) {
    return new OptionalNumber(OptionalDouble.of(Double.parseDouble(value)));
  }

  public static OptionalNumber empty() {
    return empty;
  }

  @Override
  public boolean isEmpty() {
    return value.isEmpty();
  }

  @Override
  public boolean isPresent() {
    return !isEmpty();
  }

  @Override
  public Double getAsTyped() {
    if (value.isPresent()) {
      return this.value.getAsDouble();
    } else {
      throw new NoSuchElementException("No value present");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OptionalNumber that = (OptionalNumber) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
