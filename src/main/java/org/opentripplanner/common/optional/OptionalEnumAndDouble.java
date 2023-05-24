package org.opentripplanner.common.optional;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalDouble;
import javax.validation.constraints.NotNull;

public class OptionalEnumAndDouble<T extends Enum<T>> implements OptionalValue, Serializable {

  private final OptionalEnum<T> optionalEnum;
  private final OptionalDouble optionalDouble;

  private OptionalEnumAndDouble(
    @NotNull OptionalEnum<T> optionalEnum,
    @NotNull OptionalDouble optionalDouble
  ) {
    this.optionalEnum = optionalEnum;
    this.optionalDouble = optionalDouble;
  }

  private OptionalEnumAndDouble(@NotNull OptionalEnum<T> optionalEnum) {
    this(optionalEnum, OptionalDouble.empty());
  }

  private OptionalEnumAndDouble(@NotNull OptionalDouble optionalDouble) {
    this(OptionalEnum.empty(), optionalDouble);
  }

  public static <E extends Enum<E>> OptionalEnumAndDouble<E> get(String value, Class<E> enumClass)
    throws Exception {
    OptionalEnumAndDouble<E> result;
    try {
      result = new OptionalEnumAndDouble<>(OptionalEnum.get(value, enumClass));
    } catch (Exception exc) {
      result = new OptionalEnumAndDouble<>(OptionalDouble.of(Double.parseDouble(value)));
    }
    return result;
  }

  public static <E extends Enum<E>> OptionalEnumAndDouble<E> empty() {
    return new OptionalEnumAndDouble<E>(OptionalEnum.empty(), OptionalDouble.empty());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OptionalEnumAndDouble that = (OptionalEnumAndDouble) o;
    return (
      Objects.equals(optionalEnum, that.optionalEnum) &&
      Objects.equals(optionalDouble, that.optionalDouble)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(optionalEnum, optionalDouble);
  }

  @Override
  public boolean isEmpty() {
    return this.optionalDouble.isEmpty() && this.optionalEnum.isEmpty();
  }

  @Override
  public boolean isPresent() {
    return !isEmpty();
  }

  @Override
  public Object getAsTyped() {
    if (!isPresent()) {
      throw new NoSuchElementException("No value present");
    }
    if (this.optionalEnum.isPresent()) {
      return this.optionalEnum.getAsTyped();
    }
    if (this.optionalDouble.isPresent()) {
      return this.optionalDouble.getAsDouble();
    }
    return new Object();
  }
}
