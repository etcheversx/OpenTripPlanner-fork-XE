package org.opentripplanner.openstreetmap.model;

import java.util.Objects;
import java.util.OptionalDouble;
import javax.validation.constraints.NotNull;

public class OptionalEnumAndDouble {

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

  public static OptionalEnumAndDouble get(String value) throws Exception {
    OptionalEnumAndDouble result;
    try {
      result = new OptionalEnumAndDouble(OptionalEnum.get(value));
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
}
