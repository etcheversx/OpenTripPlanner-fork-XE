package org.opentripplanner.openstreetmap.model;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalDouble;
import javax.validation.constraints.NotNull;

public class OptionalEnumAndDouble implements OptionalValue, Serializable {

  private static final OptionalEnumAndDouble _EMPTY = new OptionalEnumAndDouble(
    OptionalEnum.empty(),
    OptionalDouble.empty()
  );
  private final OptionalEnum optionalEnum;
  private final OptionalDouble optionalDouble;

  private OptionalEnumAndDouble(
    @NotNull OptionalEnum optionalEnum,
    @NotNull OptionalDouble optionalDouble
  ) {
    this.optionalEnum = optionalEnum;
    this.optionalDouble = optionalDouble;
  }

  private OptionalEnumAndDouble(@NotNull OptionalEnum optionalEnum) {
    this(optionalEnum, OptionalDouble.empty());
  }

  private OptionalEnumAndDouble(@NotNull OptionalDouble optionalDouble) {
    this(OptionalEnum.empty(), optionalDouble);
  }

  public static <E extends Enum<E>> OptionalEnumAndDouble get(String value, Class<E> enumClass)
    throws Exception {
    OptionalEnumAndDouble result;
    try {
      result = new OptionalEnumAndDouble(OptionalEnum.get(value, enumClass));
    } catch (Exception exc) {
      result = new OptionalEnumAndDouble(OptionalDouble.of(Double.parseDouble(value)));
    }
    return result;
  }

  public static OptionalEnumAndDouble empty() {
    return _EMPTY;
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
    return equals(_EMPTY);
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
