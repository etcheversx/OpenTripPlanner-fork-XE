package org.opentripplanner.openstreetmap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OptionalEnum<T extends Enum<T>> implements OptionalValue<T> {

  private static final OptionalEnum<?> empty = new OptionalEnum<>();

  private T enumerate;

  private OptionalEnum() {}

  private OptionalEnum(T enumerate) {
    this.enumerate = enumerate;
  }

  public static OptionalEnum<?> empty() {
    return empty;
  }

  public static <E extends Enum<E>> OptionalEnum<E> get(String value, Class<E> enumClass)
    throws Exception {
    try {
      E enumerate = Enum.valueOf(enumClass, value);
      return new OptionalEnum<>(enumerate);
    } catch (Exception exc) {
      throw new Exception("Invalid enum value " + value);
    }
  }

  public static <E extends Enum<E>> ArrayList<OptionalEnum<E>> parseValues(
    String values,
    Class<E> enumClass
  ) {
    ArrayList<OptionalEnum<E>> result = new ArrayList<>();
    if (values == null) {
      return result;
    }
    Arrays
      .stream(values.split(";"))
      .forEach(s -> {
        try {
          OptionalEnum<E> optionalEnum = get(s, enumClass);
          if (!result.contains(optionalEnum)) {
            result.add(optionalEnum);
          }
        } catch (Exception ignored) {}
      });
    return result;
  }

  @Override
  public boolean isEmpty() {
    return this.equals(empty);
  }

  @Override
  public boolean isPresent() {
    return !isEmpty();
  }

  @Override
  public T getAsTyped() {
    if (!isPresent()) {
      throw new NoSuchElementException("No value present");
    }
    return this.enumerate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OptionalEnum that = (OptionalEnum) o;
    return Objects.equals(enumerate, that.enumerate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enumerate);
  }
}
